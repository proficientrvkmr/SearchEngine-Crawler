package com.domain.searchengine.service;

import com.domain.searchengine.model.IndexResult;

public interface IndexService {

    IndexResult indexingOfPage(String body);

}