package org.example;

import org.example.enums.ReturnCodeSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WiseSayingTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    ReturnCodeSet checker = ReturnCodeSet.SUCCESS;

    @BeforeEach
    void setUp() {
        // 테스트 전 출력 스트림을 캡처
        System.setOut(new PrintStream(outContent));
        // 테스트 전 기존 파일 삭제
        deleteFiles();
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 출력 스트림 복원
        System.setOut(originalOut);
        // 테스트 후 생성된 파일 삭제
        deleteFiles();
    }

    // 테스트에 사용되는 파일을 정리하는 헬퍼 메서드
    private void deleteFiles() {
        File dbDir = new File("./db/wiseSaying");
        if (dbDir.exists()) {
            File[] files = dbDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

    // 사용자 입력을 시뮬레이션하기 위한 헬퍼 메서드
    private void setInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        Util.sc = new java.util.Scanner(System.in);
    }

    // 명언 등록 기능 테스트
    @Test
    void testRegisterQuote() {
        setInput("1\n미래가 진실을 말하도록 두라 내 업적과 성과는 하나하나 미래에서 평가받을 것이다\n니콜라 테슬라\n7\n"); // 1.등록 -> 명언 입력 -> 작가 입력 -> 6.종료

        Controller controller = new Controller();
        controller.runWishSay(8, 0);

        assertTrue(outContent.toString().contains(checker.getMessage()));
    }

    // 명언 조회 기능 테스트
    @Test
    void testViewQuote() {
        setInput("1\n자연은 아주 단순하다\n리처드 파인만\n2\n7\n"); // 1.등록 -> 명언 입력 -> 작가 입력 -> 2.조회 -> 7.종료
        Controller controller = new Controller();
        controller.runWishSay(8, 0);

        assertTrue(outContent.toString().contains(checker.getMessage()));
    }

    // 명언 검색 기능 테스트
    @Test
    void testSearchQuote() {
        setInput("1\n자연은 아주 단순하다\n리처드 파인만\n1\n수학은 이해하는 것이 아니라 그저 익숙해지는 것이다\n폰 노이만\n6\n수학\n7\n"); // 1.등록 -> 명언 입력 -> 작가 입력 -> 6.조회 -> 7.종료
        Controller controller = new Controller();
        controller.runWishSay(8, 0);

        assertTrue(outContent.toString().contains(checker.getMessage()));
    }

    // 명언 검색 paging 테스트
    @Test
    void testSearchQuotePaging() {
        setInput("1\n자연은 아주 단순하다\n리처드 파인만\n" +
                "1\n수학은 이해하는 것이 아니라 그저 익숙해지는 것이다\n폰 노이만\n" +
                "1\n기계가 할 수 없는 어떤 것이 있다고 주장하지만 만일 기계가 할 수 없는 것이 무엇인지 정확하게 이야기해준다면 나는 언제든지 그 일을 수행할 수 있는 기계를 만들 수 있다\n폰 노이만\n" +
                "1\n경험을 통한 학습의 가장 큰 어려움은 보통 같은 사건을 두 번 겪지 않는다는 것입니다\n폰 노이만\n" +
                "1\n자신이 무슨 말을 하고 있는지조차 모르는데 정확하게 말하는것은 의미가 없습니다\n폰 노이만\n" +
                "1\n문제를 해결하려면 문제를 인식하고 관리하고 극복해야 합니다 그러면 문제를 해결할 수 있습니다\n폰 노이만\n" +
                "1\n임의의 숫자를 생성하는 산술적 방법을 고려하는 사람은 당연히 죄의 상태에 있습니다\n폰 노이만\n" +
                "1\n나는 이제 죽음이요 세상의 파괴자가 되었도다\n줄리어스 로버트 오펜하이머\n" +
                "1\n과학이 전부는 아니지만 과학은 참으로 아름답습니다\n줄리어스 로버트 오펜하이머\n" +
                "1\n미래가 진실을 말하도록 두라 내 업적과 성과는 하나하나 미래에서 평가받을 것이다\n니콜라 테슬라\n" +
                "1\n어떤 것이든 상상이 가능한 사람은 불가능을 가능으로 바꿀 수 있다\n앨런 튜링\n" +
                "1\n명언12\n작가12\n" + // 이 부분이 누락되었을 가능성이 높습니다.
                "6\n노이만\n2\n0\n7\n");  // 1.등록 -> 명언 입력 -> 작가 입력 *10 -> 6.조회 -> 2.2페이지 조회 0.페이징 종료
        Controller controller = new Controller();
        controller.runWishSay(8, 0);

        assertTrue(outContent.toString().contains(checker.getMessage()));
    }

    // 명언 수정 기능 테스트
    @Test
    void testUpdateQuote() {
        setInput("1\n수학은 이해하는 것이 아니라 그저 익숙해지는 것이다\n폰 노이만\n3\n1\n기계가 할 수 없는 어떤 것이 있다고 주장하지만 만일 기계가 할 수 없는 것이 무엇인지 정확하게 이야기해준다면 나는 언제든지 그 일을 수행할 수 있는 기계를 만들 수 있다\n7\n"); // 1.등록 -> 명언 입력 -> 작가 입력 -> 3.수정 -> 1번 명언 -> 새 명언 입력 -> 6.종료

        Controller controller = new Controller();
        controller.runWishSay(8, 0);

        assertTrue(outContent.toString().contains(checker.getMessage()));
    }

    // 명언 삭제 기능 테스트
    @Test
    void testDeleteQuote() {
        setInput("1\n어떤 것이든 상상이 가능한 사람은 불가능을 가능으로 바꿀 수 있다\n앨런 튜링\n4\n1\n7\n"); // 1.등록 -> 명언 입력 -> 작가 입력 -> 4.삭제 -> 1번 명언 -> 7.종료

        Controller controller = new Controller();
        controller.runWishSay(8, 0);

        assertTrue(outContent.toString().contains(checker.getMessage()));
    }

    // 빌드 기능 테스트
    @Test
    void testBuildQuote() {
        setInput("1\n이론이 얼마나 거창한지는 별로 중요하지 않다\n리처드 파인만\n5\n7\n"); // 1.등록 -> 명언 입력 -> 작가 입력 -> 5.빌드 -> 6.종료

        Controller controller = new Controller();
        controller.runWishSay(8, 0);

        assertTrue(outContent.toString().contains(checker.getMessage()));
        // 빌드된 파일이 존재하는지 확인
        File builtFile = new File("./db/wiseSaying/data.json");
        assertTrue(builtFile.exists());
    }
}
