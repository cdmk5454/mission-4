package org.example;

import org.example.enums.CommendSet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Util {

    public static Scanner sc = new Scanner(System.in);

    // 명령어 처리 및 검증
    public static int getCommendInput() {
        String input = sc.nextLine();
        return validateNumberCommendInput(input);
    }
    // 사용자 입력값 처리 및 검증
    public static String getUserInput() {
        String input = sc.nextLine();
        return validateInput(input);
    }

    // 사용자 입력값 처리 및 검증 (숫자 전용)
    public static Integer getNumberInput() {
        String input = sc.nextLine();
        return validateNumberInput(input);
    }

    // 스캐너 종료
    public static void closeScanner() {
        sc.close();
    }

    public static String validateInput(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        // 한글, 영문, 숫자만 허용
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9가-힣\\s\\u3131-\u3163]+$");
        if (!pattern.matcher(input).matches()) {
            System.out.println("특수문자는 입력할 수 없습니다.");
            return null;
        }

        return input;
    }

    public static Integer validateNumberCommendInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        try {
            int result = Integer.parseInt(input.trim());
            if(result < CommendSet.REGISTER.getValue() || result > CommendSet.EXIT.getValue())  {  // 명령어 범위 체크
                System.out.println("1~7 사이의 값만 입력할 수 있습니다.");
                return null;
            }
            return result;
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력할 수 있습니다.");
            return null;
        }
    }

    public static Integer validateNumberInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력할 수 있습니다.");
            return null;
        }
    }

    public static String jsonBuilder(WiseSaying wiseSaying) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
            if (wiseSaying.isFlag()) {
                jsonBuilder.append("{");
                jsonBuilder.append("\"id\":").append(wiseSaying.getId()).append(",");
                jsonBuilder.append("\"quote\":\"").append(wiseSaying.getQuote()).append("\",");
                jsonBuilder.append("\"author\":\"").append(wiseSaying.getAuthor()).append("\",");
                jsonBuilder.append("\"flag\":").append(wiseSaying.isFlag());
                jsonBuilder.append("}");
            }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

    public static WiseSaying jsonReader(int id) {
        WiseSaying wiseSaying = null;
        String filePath = "./db/wiseSaying/" + id + ".json";

        try (FileInputStream fis = new FileInputStream(filePath);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String jsonString = jsonBuilder.toString();

            int idStart = jsonString.indexOf("\"id\":") + 5;
            int idEnd = jsonString.indexOf(",", idStart);
            int parsedId = Integer.parseInt(jsonString.substring(idStart, idEnd));

            int quoteStart = jsonString.indexOf("\"quote\":\"") + 9;
            int quoteEnd = jsonString.indexOf("\",", quoteStart);
            String parsedQuote = jsonString.substring(quoteStart, quoteEnd);

            int authorStart = jsonString.indexOf("\"author\":\"") + 10;
            int authorEnd = jsonString.indexOf("\",", authorStart);
            String parsedAuthor = jsonString.substring(authorStart, authorEnd);

            int flagStart = jsonString.indexOf("\"flag\":") + 7;
            int flagEnd = jsonString.indexOf("}", flagStart);
            boolean parsedFlag = Boolean.parseBoolean(jsonString.substring(flagStart, flagEnd));

            wiseSaying = new WiseSaying(parsedId, parsedQuote, parsedAuthor, parsedFlag);

        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("JSON 파일 파싱 중 오류가 발생했습니다. 파일 형식을 확인해주세요: " + e.getMessage());
        }

        return wiseSaying;
    }

    public static String jsonBuilderToList(List<WiseSaying> WiseSayingList) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("["); // 배열의 시작

        for (int i = 0; i < WiseSayingList.size(); i++) {
            WiseSaying wiseSaying = WiseSayingList.get(i);
            if (wiseSaying.isFlag()) { // 삭제되지 않은 명언만 추가
                jsonBuilder.append("{");
                jsonBuilder.append("\"id\":").append(wiseSaying.getId()).append(",");
                jsonBuilder.append("\"quote\":\"").append(wiseSaying.getQuote()).append("\",");
                jsonBuilder.append("\"author\":\"").append(wiseSaying.getAuthor()).append("\",");
                jsonBuilder.append("\"flag\":").append(wiseSaying.isFlag());
                jsonBuilder.append("}");

                if (i < WiseSayingList.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
        }

        jsonBuilder.append("]"); // 배열의 끝
        return jsonBuilder.toString();
    }

    // 공통 메뉴 출력
    public static void showAppHeader() {
        System.out.println("== 명언 앱 ==");
    }

    public static void showMenuCommands(String commands) {
        System.out.println("명령어: " + commands);
    }

    public static void showUserCommand(int commandType) {
        String command = CommendSet.getKeyByValue(commandType);
        System.out.println("명령) " + command);
    }

    public static void showMessage(String Message) {
        System.out.println(Message);
    }

}