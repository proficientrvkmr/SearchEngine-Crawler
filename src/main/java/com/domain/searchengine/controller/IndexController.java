package com.domain.searchengine.controller;

import com.domain.searchengine.model.IndexResult;
import com.domain.searchengine.service.IndexService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

@Api
@RestController("/engine/")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @PostMapping("index")
    @ResponseStatus(HttpStatus.OK)
    public IndexResult indexingOfPage(@RequestBody String body) throws MalformedURLException, URISyntaxException {
        return indexService.indexingOfPage(body);
    }
}
