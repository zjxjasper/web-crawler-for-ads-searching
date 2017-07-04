package com.cs504.crawler;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws Exception{

        String crawlHistoryFileName = "CrawlHistory.log";
        BufferedWriter crawlHistoryBuffer = new BufferedWriter(new FileWriter(crawlHistoryFileName));

        Crawler crawler = new Crawler();
        int numQuery = crawler.initQuery();
        System.out.println("total Query number: " + numQuery);
        crawler.initIO();
        Set<Integer> queryTaskSet = new HashSet<>();
        Set<Integer> sucessSet = new HashSet<>();
        for(int i = 0; i < numQuery; i++ ) {
            queryTaskSet.add(i);
        }
        int nTry = 0;
        while(queryTaskSet.isEmpty() == false){
            for(Integer i: sucessSet){
                queryTaskSet.remove(i);
            }
            nTry++;
            System.out.println("This is try: " + nTry + ", unCrawled query: " + queryTaskSet.size() + ", last try sucess: " + sucessSet.size() );
            sucessSet.clear();
            for(Integer i: queryTaskSet){
                crawler.initProxy();
                crawler.testProxy();
                try{
                    Thread.sleep(2000);
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                String query = crawler.queryList.get(i);
                String[] queryParam = query.split(",");
                try{
                    crawler.getAmazonProds(queryParam, i);
                    sucessSet.add(i);
                    crawlHistoryBuffer.write("query " + i + " success\n");
                    System.out.print("try " + nTry + " query " + i + " success\n");
                }
                catch (Exception eCraw){
                    crawlHistoryBuffer.write("query " + i + " failed\n");
                    System.out.print("try " + nTry + " query " + i + " failed\n");
                }
            }
        }

        crawlHistoryBuffer.close();
        crawler.endIO();

    }





}
