package com.domain.searchengine.controller;

import com.domain.searchengine.model.SearchResult;
import com.domain.searchengine.service.SearchService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController("/engine")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("search")
    public SearchResult searchKey(@RequestBody String keyword) {

        return searchService.searchKey(keyword);
    }
}
