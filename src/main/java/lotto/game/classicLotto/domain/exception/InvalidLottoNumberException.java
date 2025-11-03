package lotto.game.classicLotto.domain.exception;

import lotto.game.classicLotto.view.messages.ClassicLottoErrorMessage;

public class InvalidLottoNumberException extends IllegalArgumentException {
    private final ClassicLottoErrorMessage errorMessage;

    public InvalidLottoNumberException(ClassicLottoErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public InvalidLottoNumberException(ClassicLottoErrorMessage errorMessage, Object... args) {
        super(errorMessage.format(args));
        this.errorMessage = errorMessage;
    }

    public ClassicLottoErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
