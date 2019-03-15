package com.domain.searchengine.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResult {

    private String keyword;
    private int foundResult;
    private List<ResultData> resultData;
}
