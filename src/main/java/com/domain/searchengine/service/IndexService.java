package com.domain.searchengine.service;

import com.domain.searchengine.model.IndexResult;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public interface IndexService {

    IndexResult indexingOfPage(String body) throws MalformedURLException, URISyntaxException;

}