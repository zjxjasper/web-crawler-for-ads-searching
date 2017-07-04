package com.cs504.crawler;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

public class Ad implements Serializable{
    private static final long serialVersionUID = 1L;
    public int adId;
    public int compainId;
    public List<String> keyWords;
    public double relevanceScore;
    public double pClick;
    public double bidPrice;
    public double rankScore;
    public double qualityScore;
    public double costPerClick;
    public int position; // 1:top 2:bot;

    // required from here
    public String title;
    public double price;
    public String thumbnail;        //photo url
    public String description;
    public String brand;
    public String detail_url;
    public String query;
    public int query_group_id;
    public String category;

    public Ad(){
    }

    public Ad(int id){
        this.adId = id;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public int getCompainId() {
        return compainId;
    }

    public void setCompainId(int compainId) {
        this.compainId = compainId;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(double relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public double getpClick() {
        return pClick;
    }

    public void setpClick(double pClick) {
        this.pClick = pClick;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public double getRankScore() {
        return rankScore;
    }

    public void setRankScore(double rankScore) {
        this.rankScore = rankScore;
    }

    public double getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(double qualityScore) {
        this.qualityScore = qualityScore;
    }

    public double getCostPerClick() {
        return costPerClick;
    }

    public void setCostPerClick(double costPerClick) {
        this.costPerClick = costPerClick;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getQuery_group_id() {
        return query_group_id;
    }

    public void setQuery_group_id(int query_group_id) {
        this.query_group_id = query_group_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
