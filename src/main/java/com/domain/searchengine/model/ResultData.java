package com.domain.searchengine.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultData {

    private String pageTitle;
    private String pageLink;
    private int wordOccurrence;
}
