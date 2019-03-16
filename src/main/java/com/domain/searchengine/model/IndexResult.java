package com.domain.searchengine.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IndexResult {

    private long pageAdded;
    private long keywordsAdded;

}
