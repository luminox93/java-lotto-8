package lotto.domain.exception;

import lotto.view.messages.ErrorMessage;

public class InvalidPurchaseAmountException extends IllegalArgumentException {
    private final ErrorMessage errorMessage;

    public InvalidPurchaseAmountException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public InvalidPurchaseAmountException(ErrorMessage errorMessage, Object... args) {
        super(errorMessage.format(args));
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
