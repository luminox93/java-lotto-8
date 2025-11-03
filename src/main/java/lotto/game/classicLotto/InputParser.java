package lotto.game.classicLotto;

import lotto.common.exception.InvalidInputException;
import lotto.common.exception.InvalidPurchaseAmountException;
import lotto.common.messages.CommonErrorMessage;

import java.util.Arrays;
import java.util.List;

public class InputParser {
    private static final int LOTTO_PRICE = 1000;
    private static final String NUMBER_DELIMITER = ",";

    public int parsePurchaseAmount(String input) {
        int amount = parseInteger(input);
        validatePurchaseAmount(amount);
        return amount;
    }

    private int parseInteger(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new InvalidPurchaseAmountException(CommonErrorMessage.INVALID_NUMBER_FORMAT);
        }
    }

    private void validatePurchaseAmount(int amount) {
        if (amount <= 0) {
            throw new InvalidPurchaseAmountException(CommonErrorMessage.INVALID_PURCHASE_AMOUNT_POSITIVE);
        }
        if (amount % LOTTO_PRICE != 0) {
            throw new InvalidPurchaseAmountException(CommonErrorMessage.INVALID_PURCHASE_AMOUNT, LOTTO_PRICE);
        }
    }

    public List<Integer> parseWinningNumbers(String input) {
        validateWinningNumbersFormat(input);
        String[] tokens = input.split(NUMBER_DELIMITER);
        return Arrays.stream(tokens)
                .map(String::trim)
                .map(this::parseInteger)
                .toList();
    }

    private void validateWinningNumbersFormat(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidInputException(CommonErrorMessage.INVALID_INPUT_EMPTY);
        }
        if (input.startsWith(NUMBER_DELIMITER)) {
            throw new InvalidInputException(CommonErrorMessage.INVALID_INPUT_START_COMMA);
        }
        if (input.endsWith(NUMBER_DELIMITER)) {
            throw new InvalidInputException(CommonErrorMessage.INVALID_INPUT_END_COMMA);
        }
        if (input.contains(NUMBER_DELIMITER + NUMBER_DELIMITER)) {
            throw new InvalidInputException(CommonErrorMessage.INVALID_INPUT_CONSECUTIVE_COMMA);
        }
    }

    public int parseBonusNumber(String input) {
        return parseInteger(input);
    }
}
