package lotto;

import lotto.domain.exception.InvalidPurchaseAmountException;
import lotto.view.messages.ErrorMessage;

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
            throw new InvalidPurchaseAmountException(ErrorMessage.INVALID_NUMBER_FORMAT);
        }
    }

    private void validatePurchaseAmount(int amount) {
        if (amount <= 0) {
            throw new InvalidPurchaseAmountException(ErrorMessage.INVALID_PURCHASE_AMOUNT_POSITIVE);
        }
        if (amount % LOTTO_PRICE != 0) {
            throw new InvalidPurchaseAmountException(ErrorMessage.INVALID_PURCHASE_AMOUNT, LOTTO_PRICE);
        }
    }

    public List<Integer> parseWinningNumbers(String input) {
        String[] tokens = input.split(NUMBER_DELIMITER);
        return Arrays.stream(tokens)
                .map(String::trim)
                .map(this::parseInteger)
                .toList();
    }

    public int parseBonusNumber(String input) {
        return parseInteger(input);
    }
}
