// WiseSayingRepository.java (새로 생성할 파일)

package org.example;

import org.example.enums.ReturnCodeSet;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Repository {
    public List<WiseSaying> WiseSayingList = new ArrayList<>();
    public Integer currentLastQuoteId = 0;
    final public int pageSize = 5;
    String result = "";
    ReturnCodeSet success = ReturnCodeSet.SUCCESS;
    ReturnCodeSet fail = ReturnCodeSet.FAIL;
    ReturnCodeSet error = ReturnCodeSet.ERROR;
    public Repository() {
        WiseSayingList = new ArrayList<>();
        loadQuotesFromFile();
    }

    public void loadQuotesFromFile() {
        String baseDir = "./db/wiseSaying/";
        int lastQuoteId = -1;

        Path lastIdPath = Paths.get(baseDir, "lastId.txt");
        if (Files.exists(lastIdPath)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(lastIdPath.toFile()))) {
                lastQuoteId = Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                lastQuoteId = -1;
            }
        }

        for (int i = 0; i <= lastQuoteId; i++) {
            Path filePath = Paths.get(baseDir, i + ".json");

            if (!Files.exists(filePath)) {
                continue;
            }

            WiseSaying wiseSaying = Util.jsonReader(i);
            if (wiseSaying.isFlag()) {
                WiseSayingList.add(wiseSaying);
                currentLastQuoteId = lastQuoteId;
            }
        }
    }


    public String viewQuote(int page) {
        StringBuilder sb = new StringBuilder();
        if(WiseSayingList.isEmpty()) {
            result = fail.getMessage();
            return result;
        }

        int startIndex = (page - 1) * pageSize;
        if (startIndex >= WiseSayingList.size()) {
            return ReturnCodeSet.FAIL.getMessage(); // 요청한 페이지가 범위를 벗어남
        }

        int endIndex = Math.min(startIndex + pageSize, WiseSayingList.size());

        for(int i = startIndex; i < endIndex; i++) {
            WiseSaying wiseSaying = WiseSayingList.get(i);
            if (wiseSaying.isFlag()) {
                sb.append(wiseSaying.getId()+1 + " / ")
                        .append(wiseSaying.getQuote())
                        .append(" / ")
                        .append(wiseSaying.getAuthor())
                        .append("\n");
            }
        }
        return sb.toString();
    }

    public String deleteQuote(int id) {

        int index = id - 1;
        Optional<WiseSaying> target = WiseSayingList.stream()
                .filter(ws -> ws.getId() == index)
                .findFirst();

        if (target.isEmpty()) {
            return error.getMessage();
        }

        WiseSaying wiseSayingToDelete = target.get();
        if (wiseSayingToDelete.isFlag()) {
            wiseSayingToDelete.setFlag(false);
            result = success.getMessage();
        } else {
            result = fail.getMessage();
        }
        return result;
    }

    public int totalPage(){
        if (WiseSayingList.isEmpty()) {
            return 0;
        }
        int totalSize = (int) WiseSayingList.stream()
                .filter(WiseSaying::isFlag)
                .count();
        int result = totalSize / pageSize;
        if (totalSize % pageSize > 0) {
            result++;
        }
        if (result == 0) {
            result = 1; // 최소 페이지는 1로 설정
        }
        return result;
    }

    public String updateQuote(int id,String quote) {
        int index = id - 1;

        if (index < 0 || index >= WiseSayingList.size()) {
            result = error.getMessage();
            return result;
        }

        WiseSaying wiseSayingToUpdate = WiseSayingList.get(index);

        if (!wiseSayingToUpdate.isFlag()) {
            result = fail.getMessage();
            return result;
        }

        wiseSayingToUpdate.setQuote(quote);
        result = success.getMessage();
        return result;
    }

    public String searchQuotes(String Keyword,int page) {

        if (WiseSayingList.size() <= 0) {
            result = error.getMessage();
            return result;
        }
        StringBuilder sb = new StringBuilder();
        WiseSayingList.stream()
                .filter(wiseSaying -> wiseSaying.isFlag() &&
                        (wiseSaying.getQuote().contains(Keyword) || wiseSaying.getAuthor().contains(Keyword)))
                .skip((page - 1) * pageSize)
                .limit(pageSize)
                .forEach(wiseSaying -> {
                    sb.append(wiseSaying.getId() + 1).append(" / ")
                            .append(wiseSaying.getQuote()).append(" / ")
                            .append(wiseSaying.getAuthor()).append("\n");
                });
        if (sb.length() == 0) {
            result = fail.getMessage();
            return result;
        }
        result = sb.toString();
        return result;
    }

    public String buildQuote() {
        String absolutePath = "./db/wiseSaying/data.json";
        try {
            Path filePath = Paths.get(absolutePath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            FileOutputStream fileOutputStream = new FileOutputStream(absolutePath);
            String word = Util.jsonBuilderToList(WiseSayingList);
            byte b[] = word.getBytes();
            fileOutputStream.write(b);
            fileOutputStream.close();
            result =success.getMessage();
            return result;
        } catch (Exception e) {
            result = "ERROR: " + e.getMessage();
            return result;
        }
    }

    public void makeJsonFile() {
        String absolutePath = "./db/wiseSaying/";
        try {
            if(!WiseSayingList.isEmpty()){
                for(WiseSaying wiseSaying : WiseSayingList) {
                    FileOutputStream fileOutputStream = new FileOutputStream(absolutePath+wiseSaying.getId()+".json");
                    String word = Util.jsonBuilder(wiseSaying);
                    byte b[] = word.getBytes();
                    fileOutputStream.write(b);
                    fileOutputStream.close();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(absolutePath+"lastId.txt");
                String word = currentLastQuoteId.toString();
                byte b[] = word.getBytes();
                fileOutputStream.write(b);
                fileOutputStream.close();
            }
        } catch (Exception e) {
        }
    }

    public void CurrentLastQuoteIdPlus() {
        currentLastQuoteId++;
    }

    public String addWiseSayingList(WiseSaying wiseSaying) {
        WiseSayingList.add(wiseSaying);
        result = success.getMessage();
        return result;
    }
}