package com.domain.searchengine.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PageTitleExtractor {

    private static final String HTML_TITLE_PATTERN = "<title>(.*?)</title>";
    private Pattern patternTag;
    private Matcher matcherTag;


    public PageTitleExtractor() {
        patternTag = Pattern.compile(HTML_TITLE_PATTERN);
    }

    public String grabPageTitle(final String htmlContent) {
        String pageTitle = null;
        matcherTag = patternTag.matcher(htmlContent);

        if (matcherTag.find()) {
            pageTitle = matcherTag.group(1);
        }

        return pageTitle;
    }


}
