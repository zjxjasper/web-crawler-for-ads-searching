package com.cs504.crawler;

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
        numQuery = 97;
        System.out.println("total Query number: " + numQuery);
        crawler.initIO();
        int numPage = crawler.initPage();
        Set<Integer> queryTaskSet = new HashSet<>();
        Set<Integer> sucessSet = new HashSet<>();
        for(int i = 96*3; i < 97*3; i++ ) {
            queryTaskSet.add(i);
        }
        for(int i = 213*3; i < 214*3; i++ ) {
            queryTaskSet.add(i);
        }
        /*
        for(int i = 0; i < numQuery*numPage; i++ ) {
            queryTaskSet.add(i);
        }
        */
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
                Integer indexQuery = i/numPage;
                Integer indexPage = i%numPage + 1;
                String query = crawler.queryList.get(indexQuery);
                String[] queryParam = query.split(",");
                try{
                    crawler.getAmazonProds(queryParam, indexQuery, indexPage);
                    sucessSet.add(i);
                    crawlHistoryBuffer.write("query " + indexQuery + ", page:" + indexPage + " success\n");
                    System.out.print("try " + nTry + " query " + indexQuery + ", page:" + indexPage + " success\n");
                }
                catch (Exception eCraw){
                    crawlHistoryBuffer.write("query " + indexQuery + ", page:" + indexPage + " failed\n");
                    System.out.print("try " + nTry + " query " + indexQuery + ", page:" + indexPage + " failed\n");
                }
            }
        }

        crawlHistoryBuffer.close();
        crawler.endIO();

    }
}
