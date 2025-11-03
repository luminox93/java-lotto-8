package lotto.game.classicLotto.view.messages;

public enum ErrorMessage {
    INVALID_PURCHASE_AMOUNT("[ERROR] 구입 금액은 %d원 단위여야 합니다."),
    INVALID_PURCHASE_AMOUNT_POSITIVE("[ERROR] 구입 금액은 양수여야 합니다."),
    INVALID_NUMBER_FORMAT("[ERROR] 숫자 형식이 올바르지 않습니다."),
    INVALID_LOTTO_SIZE("[ERROR] 로또 번호는 %d개여야 합니다."),
    INVALID_NUMBER_RANGE("[ERROR] 로또 번호는 %d부터 %d 사이의 숫자여야 합니다."),
    DUPLICATE_NUMBERS("[ERROR] 로또 번호는 중복될 수 없습니다."),
    DUPLICATE_BONUS_NUMBER("[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다.");

    private static final String ERROR_PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

    public String getMessage() {
        return message;
    }
}
