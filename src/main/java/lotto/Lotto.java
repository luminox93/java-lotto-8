package lotto;

import lotto.domain.exception.InvalidLottoNumberException;
import lotto.view.messages.ErrorMessage;

import java.util.HashSet;
import java.util.List;

public class Lotto {
    private static final int LOTTO_SIZE = 6;
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 45;

    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        assert numbers != null : "numbers는 null일 수 없습니다";
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        validateSize(numbers);
        validateRange(numbers);
        validateDuplicate(numbers);
    }

    private void validateSize(List<Integer> numbers) {
        if (numbers.size() != LOTTO_SIZE) {
            throw new InvalidLottoNumberException(ErrorMessage.INVALID_LOTTO_SIZE, LOTTO_SIZE);
        }
    }

    private void validateRange(List<Integer> numbers) {
        if (numbers.stream().anyMatch(number -> number < MIN_NUMBER || number > MAX_NUMBER)) {
            throw new InvalidLottoNumberException(ErrorMessage.INVALID_NUMBER_RANGE, MIN_NUMBER, MAX_NUMBER);
        }
    }

    private void validateDuplicate(List<Integer> numbers) {
        if (numbers.size() != new HashSet<>(numbers).size()) {
            throw new InvalidLottoNumberException(ErrorMessage.DUPLICATE_NUMBERS);
        }
    }

    public int countMatches(List<Integer> winningNumbers) {
        assert winningNumbers != null : "winningNumbers는 null일 수 없습니다";

        int matchCount = (int) numbers.stream()
                .filter(winningNumbers::contains)
                .count();

        assert matchCount >= 0 && matchCount <= LOTTO_SIZE : "일치 개수는 0~6 범위여야 합니다";
        return matchCount;
    }

    public boolean contains(int number) {
        return numbers.contains(number);
    }

    public List<Integer> getNumbers() {
        return List.copyOf(numbers);
    }
}
