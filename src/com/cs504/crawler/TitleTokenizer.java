package com.cs504.crawler;


import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.RemoveDuplicatesTokenFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TitleTokenizer {

    public List<String> tokenNize(String inputStr) throws Exception{


        Analyzer analyzer = new StandardAnalyzer();

        TokenStream stream = analyzer.tokenStream(null, new StringReader(inputStr));
        stream = new SnowballFilter(stream,"English");
        CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
        List<String> keywords = new ArrayList<>();
        Set<String> tokens = new HashSet<>();
        String keyword;
        stream.reset();
        while (stream.incrementToken()) {
            keyword = cattr.toString();
            if (tokens.contains(keyword) == false) {
                tokens.add(keyword);
                keywords.add(keyword);
            }
        }
        stream.end();
        stream.close();
        return keywords;
    }

}


