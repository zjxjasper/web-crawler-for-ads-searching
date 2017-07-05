package com.cs504.crawler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.management.ListenerNotFoundException;
import javax.xml.crypto.Data;
import java.io.*;
import java.lang.reflect.Executable;
import java.math.BigDecimal;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Crawler {
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private final String authUser = "bittiger";
    private final String authPassword = "cs504";
    private static final String AMAZON_QUERY_URL = "https://www.amazon.com/s/ref=nb_sb_noss?field-keywords=";
    private static final String proxyListFile = "resource/proxylist.csv";
    private static final String queryListFile = "resource/rawQuery.txt";
    private static final String envFileName = "resource/crawling_env.inp";
    private static Integer pageRange = 1;
    private static final Integer maxPage = 10;
    private List<String> proxyList = new ArrayList<>();
    public List<String> queryList = new ArrayList<>();
    private static final boolean systemPrint = false;

    private static final String logFileName = "crawl_result.log";
    private static final String dataFileName = "crawl_result.data";
    private static final String jsonFileName = "crawl_result.json";
    private static BufferedWriter dataBufferedWriter;
    private static BufferedWriter logBufferedWriter;
    private static BufferedWriter jsonBufferedWriter;
    private int adId = 0;

    public void initProxy() {
        String line = null;
        try{
            FileReader fileReader = new FileReader(proxyListFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                proxyList.add(line);
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("unable to open '" + proxyListFile + "'");
        }
        catch(IOException ex){
            System.out.println("Error reading file '" + proxyListFile + "'");
        }


        int randomIndex = new Random().nextInt(proxyList.size() - 1);
        String[] proxyParam = proxyList.get(randomIndex).split(",");


        //System.setProperty("http.proxyUser", authUser);
        //System.setProperty("http.proxyPassword", authPassword);

        System.setProperty("http.proxyHost", proxyParam[0]); // set proxy server
        System.setProperty("http.proxyPort", proxyParam[1]); // set proxy port
        System.setProperty("http.proxyUser", proxyParam[3]);
        System.setProperty("http.proxyPassword", proxyParam[4]);

        //System.setProperty("http.proxyHost", "199.101.97.159"); // set proxy server
        //System.setProperty("http.proxyPort", "60099"); // set proxy port

        Authenticator.setDefault(
                new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                authUser, authPassword.toCharArray());
                    }
                }
        );
    }

    public void testProxy() {

        String test_url = "http://www.toolsvoid.com/what-is-my-ip-address";
        try {
            HashMap<String,String> headers = new HashMap<String,String>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            headers.put("Accept-Language", "en-US,en;q=0.8");
            Document doc = Jsoup.connect(test_url).headers(headers).userAgent(USER_AGENT).timeout(10000).get();
            String iP = doc.select("body > section.articles-section > div > div > div > div.col-md-8.display-flex > div > div.table-responsive > table > tbody > tr:nth-child(1) > td:nth-child(2) > strong").first().text(); //get used IP.
            System.out.println("IP-Address: " + iP);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int initQuery() {
        String line = null;
        try{
            FileReader fileReader = new FileReader(queryListFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                if(line.length() > 0){
                    queryList.add(line);
                }
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("unable to open '" + queryListFile + "'");
        }
        catch(IOException ex){
            System.out.println("Error reading file '" + queryListFile + "'");
        }
        return queryList.size();
    }

    public int initPage() {
        int pageRange = 0;
        String line = null;
        try{
            FileReader fileReader = new FileReader(envFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            pageRange =  Integer.valueOf(bufferedReader.readLine());
            System.out.println("pageRange is set to: " + pageRange);
            bufferedReader.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("unable to open '" + envFileName + "'");
        }
        catch(IOException ex){
            System.out.println("Error reading file '" + envFileName + "'");
        }
        return pageRange;
    }

    public void initIO() {
        try{
            dataBufferedWriter = new BufferedWriter(new FileWriter(dataFileName));
            logBufferedWriter = new BufferedWriter(new FileWriter(logFileName));
            jsonBufferedWriter = new BufferedWriter(new FileWriter(jsonFileName));
        }
        catch (IOException eIO) {
            eIO.printStackTrace();
        }
    }

    public void endIO() {
        try{
            dataBufferedWriter.close();
            logBufferedWriter.close();
            jsonBufferedWriter.close();
        }
        catch (IOException eIO) {
            eIO.printStackTrace();
        }

    }

    public void getAmazonProds(String[] queryParam, Integer index, Integer page) throws Exception {

        String query = queryParam[0];
        //query = "Nikon d3400";
        Double bidPrice = Double.valueOf(queryParam[1]);
        Integer compainID = Integer.valueOf(queryParam[2].trim());
        Integer query_group_id = Integer.valueOf(queryParam[3].trim());

        HashMap<String,String> headers = new HashMap<String,String>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        //headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "en-US,en;q=0.8");
        String url = AMAZON_QUERY_URL + query + "&page=" + page.toString();
        System.out.println("url: " + url);
        try {
            Document doc = Jsoup.connect(url).maxBodySize(0).headers(headers).userAgent(USER_AGENT).timeout(10000).get();
            Integer docSize = doc.text().length();
            System.out.println("page size: " + docSize);
            Elements prods = doc.getElementsByClass("s-result-item celwidget ");

            dataBufferedWriter.write("\n");
            Integer check = prods.size()/prods.size();

            System.out.println("query " + index.toString() + " page: " + page + ",target: " + query);
            System.out.println("number of prod: " + prods.size());

            dataBufferedWriter.write("query " + index.toString() + " page: " + page +  ",target: " + query + "\n");
            dataBufferedWriter.write("Number of prod: " + prods.size() + "\n");

            Integer startId = Integer.valueOf(doc.select("li[id^=result]").attr("id").substring(7));
            System.out.println("Prod start Id: " + startId);
            for(Integer i = startId; i < startId + prods.size(); i++){
                String id = "result_" + i.toString();
                Element prodsById = doc.getElementById(id);

                String asin = prodsById.attr("data-asin");
                if(systemPrint) {
                    System.out.println("prod asin: " + asin);
                }
                dataBufferedWriter.write(asin + "\n");
                try {
                    Elements titleEleList = prodsById.getElementsByAttribute("title");
                    String prodTitle = "";
                    for (Element titleEle : titleEleList) {
                        if(systemPrint) {
                            System.out.print("prod title 1: " + titleEle.attr("title"));
                        }
                        dataBufferedWriter.write(titleEle.attr("title"));
                        prodTitle += titleEle.attr("title");
                    }
                    System.out.print("prod title 2: " + prodTitle + "\n");


                    dataBufferedWriter.write("\n");
                    String cssQueryUrl = "#result_" + i.toString() + "> div > div > div > div.a-fixed-left-grid-col.a-col-left > div > div > a";
                    Elements elemsProdUrl = doc.select(cssQueryUrl);
                    String productUrl = elemsProdUrl.attr("href");
                    if(systemPrint) {
                        System.out.println("prodctUrl is: " + productUrl);
                    }
                    dataBufferedWriter.write(productUrl + "\n");


                    String cssQueryImg = "#result_" + i.toString() + " > div > div > div > div.a-fixed-left-grid-col.a-col-left > div > div > a > img";
                    Elements elemsProdImg = doc.select(cssQueryImg);
                    String productImg = elemsProdImg.attr("src");
                    if(systemPrint) {
                        System.out.println("prodctImg is: " + productImg);
                    }
                    dataBufferedWriter.write(productImg + "\n");

                    String productPrice = doc.select("#result_" + i.toString()).select("span[aria-label^=$]").attr("aria-label");

                    System.out.println("prodctPrice is: " + productPrice);
                    dataBufferedWriter.write(productPrice + "\n");
                    Double price = priceFormat(productPrice);

                    Element category = doc.select("#leftNavContainer > ul:nth-child(2) > div > li:nth-child(1) > span > a > h4").first();
                    String productCategory = category.text();
                    if(systemPrint) {
                        System.out.println("productCategory is: " + productCategory);
                    }
                    dataBufferedWriter.write(productCategory + "\n");


                    String cssQueryBrand = "#result_"+ i.toString() +" > div > div > div > div.a-fixed-left-grid-col.a-col-right > div.a-row.a-spacing-small > div:nth-child(2) > span:nth-child(2)";
                    Elements elemBrand = doc.select(cssQueryBrand);
                    String productBrand;
                    try{
                        productBrand = elemBrand.first().text();
                        if(systemPrint) {
                            System.out.println("productBrand is: " + productBrand);

                        }
                    }
                    catch(NullPointerException eNull) {
                        productBrand = "";
                        if(systemPrint) {
                            System.out.println("productBrand not found");
                        }
                    }
                    dataBufferedWriter.write(productBrand);


                    Ad ad = new Ad(adId);
                    ad.setCompainId(compainID);
                    ad.setBidPrice(bidPrice);
                    ad.setQuery_group_id(query_group_id);
                    ad.setDetail_url(productUrl);
                    ad.setCategory(productCategory);
                    ad.setBrand(productBrand);
                    ad.setThumbnail(productImg);
                    ad.setPrice(price);
                    ad.setQuery(query);
                    ad.setDescription("");
                    ad.setTitle(prodTitle);
                    TitleTokenizer titleTokenizer = new TitleTokenizer();
                    try{
                        List<String> keywords = titleTokenizer.tokenNize(prodTitle);
                        ad.setKeyWords(keywords);
                    }
                    catch (Exception eToken) {
                        eToken.printStackTrace();
                    }
                    adId++;

                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        String jsonInString = mapper.writeValueAsString(ad);
                        if(systemPrint) {
                            System.out.println(jsonInString);
                        }
                        jsonBufferedWriter.write(jsonInString + "\n");
                    }
                    catch (JsonProcessingException eJson) {
                        System.out.println("Transform ad into Json String failed");
                    }


                }
                catch (IOException eIO){
                    eIO.printStackTrace();
                }

                // https://www.amazon.com/Camera-18-55mm-70-300mm-Telephoto-Filters/dp/B01LZE8P9M/ref=sr_1_8?ie=UTF8&qid=1499105326&sr=8-8&keywords=nikon+d3400
                // https://www.amazon.com/Canon-T6-Digital-Telephoto-Accessory/dp/B01D93Z89W/ref=sr_1_13?ie=UTF8&qid=1499105326&sr=8-13&keywords=nikon+d3400
                //String productUrl = doc.getElementsByClass("a-link-normal a-text-normal").toString();
                //System.out.println("prod url: " + productUrl);
            }
            /*
            Element category = doc.select("#leftNavContainer > ul:nth-child(2) > div > li:nth-child(1) > span > a > h4").first();
            String categoryStr = category.text();
            System.out.println("prod category: " + categoryStr);
            */

        }
        catch (org.jsoup.HttpStatusException eHttp) {
            try {
                logBufferedWriter.write("For query No." + index + "got jsoup.HttpStatusException\n");
            }
            catch (IOException eIO1) {
                System.out.println("unable to write to log file with 'jsoup.HttpStatusException' ");
            }
            eHttp.printStackTrace();
        }
        catch (IOException eIO){
            try {
                logBufferedWriter.write("For query No." + index + "got IOException\n");
            }
            catch (IOException eIO2) {
                System.out.println("unable to write to log file with 'IOException' ");
            }
            eIO.printStackTrace();
        }


    }


    public void parseAmazonProdPage(String url) {
        try {
            HashMap<String,String> headers = new HashMap<String,String>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            headers.put("Accept-Language", "en-US,en;q=0.8");
            Document doc = Jsoup.connect(url).headers(headers).userAgent(USER_AGENT).timeout(10000).get();
            Element titleEle = doc.getElementById("productTitle");
            String title = titleEle.text();
            System.out.println("title: " + title);

            Element priceEle =doc.getElementById("priceblock_ourprice");
            String price = priceEle.text();
            System.out.println("price: " + price);

            //review
            //#cm-cr-dp-review-list
            Elements reviews = doc.getElementsByClass("a-expander-content a-expander-partial-collapse-content");
            System.out.println("number of reviews: " + reviews.size());
            for (Element review : reviews) {
                System.out.println("review content: " + review.text());
            }

            //#customer_review-R188VC0CBW8NLR > div:nth-child(4) > span > div > div.a-expander-content.a-expander-partial-collapse-content



        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Double priceFormat(String priceStr) throws Exception{
        Double price = 0.0;
        priceStr = priceStr.trim();
        if(priceStr.length() == 0){
            return price;
        }
        else {
            priceStr = priceStr.substring(1);
            if(priceStr.contains("-")){
                String[] prices = priceStr.split("-");
                price = Double.valueOf(prices[0]);
            }
            else{
                BigDecimal value = new BigDecimal(priceStr.replace(",", ""));
                price = value.doubleValue();
            }
        }
        return price;
    }

}
