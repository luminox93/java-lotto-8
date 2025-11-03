package lotto.view.messages;

public enum OutputMessage {
    PURCHASE_COUNT("%d개를 구매했습니다."),
    STATISTICS_HEADER("당첨 통계"),
    STATISTICS_DIVIDER("---"),
    RANK_STATISTIC("%s - %d개"),
    PROFIT_RATE("총 수익률은 %.1f%%입니다.");

    private final String message;

    OutputMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

    public String getMessage() {
        return message;
    }
}
