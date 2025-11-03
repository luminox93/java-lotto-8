package lotto.common.messages;

public enum CommonErrorMessage {
    INVALID_PURCHASE_AMOUNT("[ERROR] 구입 금액은 %d원 단위여야 합니다."),
    INVALID_PURCHASE_AMOUNT_POSITIVE("[ERROR] 구입 금액은 양수여야 합니다."),
    INVALID_NUMBER_FORMAT("[ERROR] 숫자 형식이 올바르지 않습니다."),
    INVALID_INPUT_EMPTY("[ERROR] 입력값은 비어있을 수 없습니다."),
    INVALID_INPUT_START_COMMA("[ERROR] 입력값은 쉼표로 시작할 수 없습니다."),
    INVALID_INPUT_END_COMMA("[ERROR] 입력값은 쉼표로 끝날 수 없습니다."),
    INVALID_INPUT_CONSECUTIVE_COMMA("[ERROR] 연속된 쉼표는 사용할 수 없습니다.");

    private final String message;

    CommonErrorMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

    public String getMessage() {
        return message;
    }
}
