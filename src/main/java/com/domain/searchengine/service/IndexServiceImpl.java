package com.domain.searchengine.service;

import com.domain.searchengine.model.HtmlLink;
import com.domain.searchengine.model.IndexResult;
import com.domain.searchengine.persistence.HtmlContentRepository;
import com.domain.searchengine.persistence.HtmlLinkRepository;
import com.domain.searchengine.persistence.KeywordRepository;
import com.domain.searchengine.persistence.entity.HtmlContentIndex;
import com.domain.searchengine.persistence.entity.HtmlLinkIndex;
import com.domain.searchengine.persistence.entity.KeywordIndex;
import com.domain.searchengine.util.HtmlLinkExtractor;
import com.domain.searchengine.util.KeywordsExtractor;
import com.domain.searchengine.util.PageTitleExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    private static final int DEEP_LEVEL = 3;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HtmlContentRepository htmlContentRepository;

    @Autowired
    private HtmlLinkExtractor htmlLinkExtractor;

    @Autowired
    private HtmlLinkRepository htmlLinkRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private KeywordsExtractor keywordsExtractor;

    @Autowired
    private PageTitleExtractor pageTitleExtractor;

    @Override
    public IndexResult indexingOfPage(String url) throws MalformedURLException, URISyntaxException {

        long totalPageAdded = 0;
        long totalKeywordAdded = 0;

        String mainHTML = getHtmlContent(url);
        HtmlContentIndex htmlContent = saveHtmlContent(url, mainHTML);
        ArrayList<KeywordIndex> keywordOccurrences = renderAndSaveKeywordOccurence(htmlContent.getId(), mainHTML);
        ArrayList<HtmlLink> pageLinks = renderAndSaveHtmlOutgoingLinks(htmlContent.getId(), mainHTML);

        totalKeywordAdded = keywordOccurrences.size();
        totalPageAdded = pageLinks.size();

        for (int i = 0; i < (pageLinks.size() > 5 ? 5 : pageLinks.size()); i++) {
            HtmlLink htmlLink = pageLinks.get(i);
            try {
                doIndexingForSubLinkPage(htmlLink.getLink(), DEEP_LEVEL, totalKeywordAdded, totalPageAdded);

            } catch (MalformedURLException | URISyntaxException e) {
                log.error(e.getMessage());
                continue;
            }
        }

        IndexResult result = IndexResult.builder()
                .keywordsAdded(totalKeywordAdded)
                .pageAdded(totalPageAdded)
                .build();
        return result;
    }

    private void doIndexingForSubLinkPage(String url, int timesToCall, long totalKeywordAdded, long totalPageAdded) throws MalformedURLException, URISyntaxException {
        String mainHTML = getHtmlContent(url);
        HtmlContentIndex htmlContent = saveHtmlContent(url, mainHTML);
        ArrayList<KeywordIndex> keywordOccurrences = renderAndSaveKeywordOccurence(htmlContent.getId(), mainHTML);
        ArrayList<HtmlLink> pageLinks = renderAndSaveHtmlOutgoingLinks(htmlContent.getId(), mainHTML);

        totalPageAdded = totalPageAdded + pageLinks.size();
        totalKeywordAdded = totalKeywordAdded + keywordOccurrences.size();

        if (--timesToCall > 0) {
            for (int i = 0; i < (pageLinks.size() > 5 ? 5 : pageLinks.size()); i++) {
                HtmlLink htmlLink = pageLinks.get(i);
                try {
                    doIndexingForSubLinkPage(htmlLink.getLink(), timesToCall, totalPageAdded, totalKeywordAdded);
                } catch (MalformedURLException | URISyntaxException e) {
                    log.error(e.getMessage());
                    continue;
                }
            }
        }
    }

    private String getHtmlContent(String url) throws MalformedURLException, URISyntaxException {
        URL newUrl = new URL(url);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(newUrl.toURI(), String.class);
        return responseEntity.getBody();
    }

    private HtmlContentIndex saveHtmlContent(String url, String html) {

        HtmlContentIndex htmlContentIndexEnity = HtmlContentIndex.builder()
                .url(url)
                .pageTitle(pageTitleExtractor.grabPageTitle(html))
                .build();

        HtmlContentIndex htmlContent = htmlContentRepository.findByUrl(url);
        if (htmlContent == null) {
            htmlContent = htmlContentRepository.save(htmlContentIndexEnity);
        }
        return htmlContent;
    }

    private ArrayList<HtmlLink> renderAndSaveHtmlOutgoingLinks(UUID htmlContentId, String rawHTML) {
        ArrayList<HtmlLink> links = htmlLinkExtractor.grabHTMLLinks(rawHTML);

        List<HtmlLinkIndex> possibleOutgoingLinks = links.stream().map(htmlLink -> HtmlLinkIndex.builder()
                .link(htmlLink.getLink())
                .linkText(htmlLink.getLinkText().length() > 255 ? htmlLink.getLinkText().substring(0, 250) : htmlLink.getLinkText())
                .contentId(htmlContentId)
                .build())
                .collect(Collectors.toList());

        htmlLinkRepository.saveAll(possibleOutgoingLinks);
        return links;
    }

    private ArrayList<KeywordIndex> renderAndSaveKeywordOccurence(UUID id, String html) {
        ArrayList<KeywordIndex> keywordList = new ArrayList<>();

        Map<String, Integer> keywordCountMap = keywordsExtractor.getKeywordOccurrences(html);
        for (Map.Entry keyword : keywordCountMap.entrySet()) {

            KeywordIndex keywordIndex = KeywordIndex.builder()
                    .keyword(keyword.getKey().toString())
                    .keywordOccurrence(Integer.parseInt(keyword.getValue().toString()))
                    .contentId(id)
                    .build();
            keywordList.add(keywordIndex);
        }

        keywordRepository.saveAll(keywordList);
        return keywordList;
    }
}
