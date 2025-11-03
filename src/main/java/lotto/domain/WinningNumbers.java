package lotto.domain;

import lotto.Lotto;
import lotto.domain.exception.InvalidBonusNumberException;
import lotto.view.messages.ErrorMessage;

import java.util.List;

public class WinningNumbers {
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 45;

    private final Lotto winningNumbers;
    private final int bonusNumber;

    public WinningNumbers(List<Integer> winningNumbers, int bonusNumber) {
        this.winningNumbers = new Lotto(winningNumbers);
        validateBonusNumber(bonusNumber);
        this.bonusNumber = bonusNumber;
    }

    private void validateBonusNumber(int bonusNumber) {
        validateBonusRange(bonusNumber);
        validateBonusDuplicate(bonusNumber);
    }

    private void validateBonusRange(int bonusNumber) {
        if (bonusNumber < MIN_NUMBER || bonusNumber > MAX_NUMBER) {
            throw new InvalidBonusNumberException(ErrorMessage.INVALID_NUMBER_RANGE, MIN_NUMBER, MAX_NUMBER);
        }
    }

    private void validateBonusDuplicate(int bonusNumber) {
        if (winningNumbers.contains(bonusNumber)) {
            throw new InvalidBonusNumberException(ErrorMessage.DUPLICATE_BONUS_NUMBER);
        }
    }

    public Rank match(Lotto lotto) {
        int matchCount = lotto.countMatches(winningNumbers.getNumbers());
        boolean bonusMatch = lotto.contains(bonusNumber);
        return Rank.of(matchCount, bonusMatch);
    }
}
