package org.example.enums;

public enum ReturnCodeSet {
    SUCCESS(200, "성공적으로 처리되었습니다."),
    FAIL(400, "잘못된 입력입니다."),
    ERROR(500, "처리 중 오류가 발생했습니다.");

    private final int code;
    private final String message;

    ReturnCodeSet(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}