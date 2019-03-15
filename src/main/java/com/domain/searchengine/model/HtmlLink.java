package com.domain.searchengine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HtmlLink {

    String link;
    String linkText;

    @Override
    public String toString() {
        return new StringBuffer("Link : ").append(this.link)
                .append(" Link Text : ").append(this.linkText).toString();
    }

    private String replaceInvalidChar(String link) {
        link = link.replaceAll("'", "");
        link = link.replaceAll("\"", "");
        return link;
    }

}