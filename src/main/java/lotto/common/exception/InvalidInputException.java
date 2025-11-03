package lotto.common.exception;

import lotto.common.messages.CommonErrorMessage;

public class InvalidInputException extends IllegalArgumentException {
    private final CommonErrorMessage errorMessage;

    public InvalidInputException(CommonErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public InvalidInputException(CommonErrorMessage errorMessage, Object... args) {
        super(errorMessage.format(args));
        this.errorMessage = errorMessage;
    }

    public CommonErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
