package com.domain.searchengine.util;

import com.domain.searchengine.model.HtmlLink;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HtmlLinkExtractor {

    private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
    private static final String HTML_A_HREF_TAG_PATTERN =
            "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
    private Pattern patternTag, patternLink;
    private Matcher matcherTag, matcherLink;


    public HtmlLinkExtractor() {
        patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
        patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
    }

    /**
     * Validate html with regular expression
     *
     * @param html html content for validation
     * @return Vector links and link text
     */
    public ArrayList<HtmlLink> grabHTMLLinks(final String html) {

        ArrayList<HtmlLink> result = new ArrayList<HtmlLink>();

        matcherTag = patternTag.matcher(html);

        while (matcherTag.find()) {

            String href = matcherTag.group(1); // href
            String linkText = matcherTag.group(2); // link text

            matcherLink = patternLink.matcher(href);

            while (matcherLink.find()) {

                String link = matcherLink.group(1); // link
                HtmlLink obj = new HtmlLink();
                try {
                    link = link.replaceAll("^\"|\"$", ""); // trim double quotes
                    link = link.replaceAll("^\'|\'$", ""); // trim single quotes
                    URL linkURL = new URL(link);
                    obj.setLink(String.valueOf(linkURL.getProtocol() + "://" + linkURL.getHost() + linkURL.getPath()));
                } catch (MalformedURLException e) {
                    System.err.println(e.getMessage());
                    continue;
                }
                obj.setLinkText(linkText.replaceAll("^\"|\"$", ""));

                result.add(obj);

            }

        }

        return result;

    }


}
