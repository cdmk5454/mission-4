package org.example;

import java.util.HashMap;
import java.util.Map;

public class Service {
    Repository repository;
    String result;
    int totalPage = 0;

    public Service() {
        this.repository = new Repository();
    }


    public String register(String quote, String author) {
        WiseSaying wiseSaying = new WiseSaying(repository.currentLastQuoteId, quote, author, true);
        repository.CurrentLastQuoteIdPlus();
        result = repository.addWiseSayingList(wiseSaying);
        return result;
    }

    public Map<String,String> viewQuote(int page) {
        result =repository.viewQuote(page);
        totalPage = repository.totalPage();
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("result",result);
        resultMap.put("totalPage",String.valueOf(totalPage));
        return resultMap;
    }

    public Map<String,String> searchQuotes(String keyWord,int page) {
        result = repository.searchQuotes(keyWord,page);
        totalPage = repository.totalPage();
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("result",result);
        resultMap.put("totalPage",String.valueOf(totalPage));
        return resultMap;
    }

    public String updateQuote(int id, String quote) {
        result = repository.updateQuote(id, quote);
        return result;
    }

    public String deleteQuote(int id) {
        result = repository.deleteQuote(id);
        return result;
    }

    public String buildQuote() {
        result = repository.buildQuote();
        return result;
    }

    public void makeJsonFile() {
        repository.makeJsonFile();
    }

}