package lotto.game.classicLotto.domain;

import camp.nextstep.edu.missionutils.Randoms;
import lotto.game.classicLotto.Lotto;

import java.util.List;
import java.util.stream.Stream;

public class LottoGenerator {
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 45;
    private static final int NUMBER_COUNT = 6;

    public List<Lotto> generate(int count) {
        assert count > 0 : "생성 개수는 양수여야 합니다";

        return Stream.generate(this::createLotto)
                .limit(count)
                .toList();
    }

    private Lotto createLotto() {
        List<Integer> numbers = Randoms.pickUniqueNumbersInRange(MIN_NUMBER, MAX_NUMBER, NUMBER_COUNT);
        return new Lotto(numbers);
    }
}
