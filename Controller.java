package org.example;

import org.example.enums.ModSet;
import org.example.enums.ReturnCodeSet;

import java.util.HashMap;
import java.util.Map;

public class Controller {
    Service service;
    int currentPage = 1; // 현재 페이지 번호
    int pageSize = 5;
    Map<String, String> resultMap = new HashMap<>();
    ReturnCodeSet success = ReturnCodeSet.SUCCESS;

    public Controller() {
        this.service = new Service();
    }

    private String processQuoteRegistration(int level) {
        Util.showMessage("명언을 입력해주세요.");
        String quote = Util.getUserInput();
        Util.showMessage("명언 :" + quote);

        Util.showMessage("작가를 입력해주세요.");
        String author = Util.getUserInput();
        Util.showMessage("작가 :" + author);
        return service.register(quote, author);
    }

    private String processQuoteUpdate() {
        String result;
        int id;

        try {
            Util.showMessage("수정하실 명언 번호를 입력해주세요.");
            id = Integer.parseInt(Util.getUserInput());
            Util.showMessage("수정할 명언 번호 :" + id);
        } catch (NumberFormatException e) {
            result = "숫자만 입력할 수 있습니다.";
            return result;
        }

        Util.showMessage("수정하실 명언 내용을 입력해주세요.");
        String quote = Util.getUserInput();
        Util.showMessage("수정할 명언 :" + quote);

        if (quote == null || quote.trim().isEmpty()) {
            result = "명언 내용은 비워둘 수 없습니다.";
            return result;
        }

        return service.updateQuote(id, quote);
    }

    private String processQuoteDelete() {
        String result;
        int id;
        try {
            Util.showMessage("삭제하실 명언 번호를 입력해주세요.");
            id = Integer.parseInt(Util.getUserInput());
            Util.showMessage("삭제하실 명언 번호:" + (id));
        } catch (NumberFormatException e) {
            result = "숫자만 입력할 수 있습니다.";
            return result;
        }
        return service.deleteQuote(id);
    }

    public void runWishSay(int level , int mod) {
        if (mod == ModSet.MAIN.getValue()) {
            Util.showAppHeader();
        }

        while(true) {
            Util.showMessage("다음 명령을 내려주세요.");
            Util.showMenuCommands("1.등록 2.조회 3.수정 4.삭제 5.빌드 6.검색 7.종료");
            int commandType = -1;
            try {
                commandType = Util.getCommendInput();
            } catch (NullPointerException e) {
                continue;
            }
            Util.showUserCommand(commandType);

            switch (commandType) {
                case 1:
                    Util.showMessage(processQuoteRegistration(level));
                    break;
                case 2:
                    handleQuoteView();
                    break;
                case 3:
                    Util.showMessage(processQuoteUpdate());
                    break;
                case 4:
                    Util.showMessage(processQuoteDelete());
                    break;
                case 5:
                    Util.showMessage(service.buildQuote());
                    break;
                case 6:
                    handleQuoteSearch();
                    break;
                case 7:
                    Util.closeScanner();
                    service.makeJsonFile();
                    return;
            }
        }
    }

    // 명언 조회 페이징 처리를 위한 메서드
    private void handleQuoteView() {
        currentPage = 1; // 조회 시작 시 현재 페이지 초기화
        while (true) {
            resultMap = service.viewQuote(currentPage);
            Util.showMessage(resultMap.get("result"));

            int totalPage = Integer.parseInt(resultMap.get("totalPage"));
            if (totalPage > 1) {
                Util.showMessage("페이지 : [" + currentPage + "] / " + totalPage + " (종료 : 0)");
                int nextPageCommand = Util.getNumberInput();

                if (nextPageCommand == 0) {
                    Util.showMessage(success.getMessage());
                    break;
                } else if (nextPageCommand > totalPage || nextPageCommand < 1) {
                    Util.showMessage("잘못된 입력입니다. 다시 시도해주세요.");
                } else {
                    currentPage = nextPageCommand;
                }
            } else {
                Util.showMessage(success.getMessage());
                break; // 페이지가 1개뿐이면 루프를 종료
            }
        }
    }

    // 명언 검색 페이징 처리를 위한 메서드
    private void handleQuoteSearch() {
        Util.showMessage("검색할 명언을 입력해주세요.");
        String keyWord = Util.getUserInput();
        Util.showMessage("검색할 명언 :" + keyWord);

        currentPage = 1;
        while(true) {
            resultMap = service.searchQuotes(keyWord, currentPage);
            Util.showMessage(resultMap.get("result"));

            int totalPage = Integer.parseInt(resultMap.get("totalPage"));

            if (totalPage > 1) {
                Util.showMessage("페이지 : [" + currentPage + "] / " + totalPage + " (종료 : 0)");
                int nextPageCommand = Util.getNumberInput();
                if (nextPageCommand == 0) {
                    Util.showMessage(success.getMessage());
                    break;
                } else if (nextPageCommand > totalPage || nextPageCommand < 1) {
                    Util.showMessage("잘못된 입력입니다. 다시 시도해주세요.");
                } else {
                    currentPage = nextPageCommand;
                }
            } else {
                Util.showMessage(success.getMessage());
                break; // 페이지가 1개뿐이면 루프를 종료
            }
        }
    }
}