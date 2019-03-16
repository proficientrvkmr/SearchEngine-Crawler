package com.domain.searchengine.service;

import com.domain.searchengine.model.ResultData;
import com.domain.searchengine.model.SearchResult;
import com.domain.searchengine.persistence.HtmlContentRepository;
import com.domain.searchengine.persistence.KeywordRepository;
import com.domain.searchengine.persistence.entity.KeywordIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private HtmlContentRepository htmlContentRepository;

    @Override
    public SearchResult searchKey(String keyword) {

        List<KeywordIndex> keywordIndexList = keywordRepository.findByKeyword(keyword);

        SearchResult searchResult = SearchResult.builder()
                .keyword(keyword)
                .foundResult(keywordIndexList.size())
                .resultData(getResultData(keywordIndexList))
                .build();

        return searchResult;
    }

    private List<ResultData> getResultData(List<KeywordIndex> keywordIndexList) {

        List<ResultData> resultData = keywordIndexList.stream()
                .map(this::convertIntoResultSet)
                .collect(Collectors.toList());

        return resultData;
    }

    private ResultData convertIntoResultSet(KeywordIndex keywordIndex) {
        return ResultData.builder()
                .pageLink(htmlContentRepository.findById(keywordIndex.getContentId()).get().getUrl())
                .pageTitle(htmlContentRepository.findById(keywordIndex.getContentId()).get().getPageTitle())
                .wordOccurrence(keywordIndex.getKeywordOccurrence())
                .build();
    }
}
