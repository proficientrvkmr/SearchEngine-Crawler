package com.domain.searchengine.service;

import com.domain.searchengine.model.SearchResult;

public interface SearchService {

    SearchResult searchKey(String keyword);
}
