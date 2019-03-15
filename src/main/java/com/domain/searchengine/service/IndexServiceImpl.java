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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IndexServiceImpl implements IndexService {

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

    @Override
    public IndexResult indexingOfPage(String url) {

        String mainHTML = getHtmlContent(url);
        HtmlContentIndex htmlContent = saveHtmlContent(url, mainHTML);
        ArrayList<KeywordIndex> keywordOccurences = renderAndSaveKeywordOccurence(htmlContent.getId(), mainHTML);
        ArrayList<HtmlLink> pageLinks = renderAndSaveHtmlOutgoingLinks(htmlContent.getId(), mainHTML);

        IndexResult result = IndexResult.builder()
                .keywordsAdded(keywordOccurences.size())
                .pageAdded(pageLinks.size())
                .build();
        return result;
    }

    private String getHtmlContent(String url) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity.getBody();
    }

    private HtmlContentIndex saveHtmlContent(String url, String html) {

        HtmlContentIndex htmlContentIndexEnity = HtmlContentIndex.builder()
                .url(url)
                .htmlContent(html)
                .build();

        HtmlContentIndex htmlContent = htmlContentRepository.save(htmlContentIndexEnity);
        return htmlContent;
    }

    private ArrayList<HtmlLink> renderAndSaveHtmlOutgoingLinks(UUID htmlContentId, String rawHTML) {
        ArrayList<HtmlLink> links = htmlLinkExtractor.grabHTMLLinks(rawHTML);

        List<HtmlLinkIndex> possibleOutgoingLinks = links.stream().map(htmlLink -> HtmlLinkIndex.builder()
                .link(htmlLink.getLink())
                .linkText(htmlLink.getLinkText())
                .contentId(htmlContentId)
                .build())
                .collect(Collectors.toList());

        htmlLinkRepository.saveAll(possibleOutgoingLinks);
        return links;
    }

    private ArrayList<HtmlLink> renderOutgoingLinks(String subUrl, int deepLevel) {

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(subUrl, String.class);
        String rawHTML = responseEntity.getBody();
        ArrayList<HtmlLink> links = htmlLinkExtractor.grabHTMLLinks(rawHTML);
        return links;
    }


    private ArrayList<KeywordIndex> renderAndSaveKeywordOccurence(UUID id, String html) {
        ArrayList<KeywordIndex> keywordList = new ArrayList<>();

        Map<String, Integer> keywordCountMap = keywordsExtractor.getKeywordOccurrences(html);
        for (Map.Entry keyword : keywordCountMap.entrySet()) {

            KeywordIndex keywordIndex = KeywordIndex.builder()
                    .keyword(keyword.getKey().toString())
                    .keywordOccurence(Integer.parseInt(keyword.getValue().toString()))
                    .contentId(id)
                    .build();
            keywordList.add(keywordIndex);
        }

        keywordRepository.saveAll(keywordList);
        return keywordList;
    }
}
