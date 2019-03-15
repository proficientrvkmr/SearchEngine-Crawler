package com.domain.searchengine.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class KeywordsExtractor {

    public Map<String, Integer> getKeywordOccurrences(String text) {
        Scanner sc = new Scanner(text);
        Map<String, Integer> keywordCountMap = new HashMap<>();

        try {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] words = line.split("\\W+");
                for (String word : words) {
                    word = word.toLowerCase();
                    if (keywordCountMap.containsKey(word)) {
                        int count = keywordCountMap.get(word);
                        keywordCountMap.put(word, ++count);
                    } else {
                        keywordCountMap.put(word, 1);
                    }
                }
            }
        } finally {
            sc.close();
        }
        return keywordCountMap;
    }
}
