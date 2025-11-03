package lotto.common.exception;

import lotto.common.messages.CommonErrorMessage;

public class InvalidPurchaseAmountException extends IllegalArgumentException {
    private final CommonErrorMessage errorMessage;

    public InvalidPurchaseAmountException(CommonErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public InvalidPurchaseAmountException(CommonErrorMessage errorMessage, Object... args) {
        super(errorMessage.format(args));
        this.errorMessage = errorMessage;
    }

    public CommonErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
