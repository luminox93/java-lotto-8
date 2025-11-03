package lotto.game.classicLotto.domain.exception;

import lotto.game.classicLotto.view.messages.ErrorMessage;

public class InvalidLottoNumberException extends IllegalArgumentException {
    private final ErrorMessage errorMessage;

    public InvalidLottoNumberException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public InvalidLottoNumberException(ErrorMessage errorMessage, Object... args) {
        super(errorMessage.format(args));
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
