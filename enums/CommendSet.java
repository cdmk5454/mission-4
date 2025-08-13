package org.example.enums;

public enum CommendSet {
    REGISTER(1,"등록"),
    VIEW(2, "조회"),
    UPDATE(3, "수정"),
    DELETE(4, "삭제"),
    BUILD(5, "빌드"),
    SEARCH(6, "검색"),
    EXIT(7, "종료");
    ;

    private final int value;
    private final String key;

    CommendSet(int value, String key) {
        this.value = value;
        this.key = key;
    }

    // 추가!
    public static String getKeyByValue(int value) {
        for (CommendSet cmd : values()) {
            if (cmd.value == value) {
                return cmd.key;
            }
        }
        return null; // 또는 예외 던지기
    }

    public String getKey(){
        return key;
    }
    public int getValue() {
        return value;
    }
}
