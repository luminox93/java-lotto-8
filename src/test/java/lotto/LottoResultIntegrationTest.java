package lotto;

import camp.nextstep.edu.missionutils.test.NsTest;
import lotto.game.classicLotto.Lotto;
import lotto.game.classicLotto.domain.LottoGenerator;
import lotto.game.classicLotto.domain.LottoResult;
import lotto.game.classicLotto.domain.Rank;
import lotto.game.classicLotto.domain.WinningNumbers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertAll;

class LottoResultIntegrationTest extends NsTest {

    @DisplayName("모든 등수가 1개씩 있는 경우 - 등수별 개수, 총 당첨금, 수익률 검증")
    @Test
    void 모든_등수가_1개씩_있는_경우() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // given
                    WinningNumbers winningNumbers = new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7);
                    LottoGenerator generator = new LottoGenerator();
                    int purchaseAmount = 6000; // 6장 구매

                    // when
                    List<Lotto> lottos = generator.generate(6);
                    LottoResult result = new LottoResult();
                    for (Lotto lotto : lottos) {
                        Rank rank = winningNumbers.match(lotto);
                        result.addRank(rank);
                    }

                    // then
                    long expectedTotalPrize = Rank.FIRST.getPrize()
                            + Rank.SECOND.getPrize()
                            + Rank.THIRD.getPrize()
                            + Rank.FOURTH.getPrize()
                            + Rank.FIFTH.getPrize();
                    double expectedProfitRate = (double) expectedTotalPrize / purchaseAmount * 100;

                    assertAll(
                            // 등수별 개수 검증
                            () -> assertThat(result.countOf(Rank.FIRST)).isEqualTo(1),
                            () -> assertThat(result.countOf(Rank.SECOND)).isEqualTo(1),
                            () -> assertThat(result.countOf(Rank.THIRD)).isEqualTo(1),
                            () -> assertThat(result.countOf(Rank.FOURTH)).isEqualTo(1),
                            () -> assertThat(result.countOf(Rank.FIFTH)).isEqualTo(1),

                            // 총 당첨금 검증
                            () -> assertThat(result.calculateTotalPrize()).isEqualTo(expectedTotalPrize),

                            // 수익률 검증
                            () -> assertThat(result.calculateProfitRate(purchaseAmount))
                                    .isCloseTo(expectedProfitRate, within(0.01))
                    );
                },
                // 당첨 번호: 1,2,3,4,5,6 / 보너스: 7
                List.of(1, 2, 3, 4, 5, 6),      // 1등 (6개 일치)
                List.of(1, 2, 3, 4, 5, 7),      // 2등 (5개 일치 + 보너스)
                List.of(1, 2, 3, 4, 5, 8),      // 3등 (5개 일치)
                List.of(1, 2, 3, 4, 10, 11),    // 4등 (4개 일치)
                List.of(1, 2, 3, 12, 13, 14),   // 5등 (3개 일치)
                List.of(10, 11, 12, 13, 14, 15) // 꽝 (0개 일치)
        );
    }

    @DisplayName("1등 대박 케이스 - 1등이 3개인 경우")
    @Test
    void 일등_대박_케이스() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // given
                    WinningNumbers winningNumbers = new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7);
                    LottoGenerator generator = new LottoGenerator();
                    int purchaseAmount = 5000; // 5장 구매

                    // when
                    List<Lotto> lottos = generator.generate(5);
                    LottoResult result = new LottoResult();
                    for (Lotto lotto : lottos) {
                        Rank rank = winningNumbers.match(lotto);
                        result.addRank(rank);
                    }

                    // then
                    long expectedTotalPrize = (long) Rank.FIRST.getPrize() * 3
                            + Rank.SECOND.getPrize()
                            + Rank.FIFTH.getPrize();
                    double expectedProfitRate = (double) expectedTotalPrize / purchaseAmount * 100;

                    assertAll(
                            () -> assertThat(result.countOf(Rank.FIRST)).isEqualTo(3),
                            () -> assertThat(result.countOf(Rank.SECOND)).isEqualTo(1),
                            () -> assertThat(result.countOf(Rank.THIRD)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.FOURTH)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.FIFTH)).isEqualTo(1),
                            () -> assertThat(result.calculateTotalPrize()).isEqualTo(expectedTotalPrize),
                            () -> assertThat(result.calculateProfitRate(purchaseAmount))
                                    .isCloseTo(expectedProfitRate, within(0.01))
                    );
                },
                List.of(1, 2, 3, 4, 5, 6),      // 1등
                List.of(1, 2, 3, 4, 5, 6),      // 1등
                List.of(1, 2, 3, 4, 5, 6),      // 1등
                List.of(1, 2, 3, 4, 5, 7),      // 2등
                List.of(1, 2, 3, 15, 16, 17)    // 5등
        );
    }

    @DisplayName("모두 꽝인 경우 - 당첨금 0원, 수익률 0%")
    @Test
    void 모두_꽝인_경우() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // given
                    WinningNumbers winningNumbers = new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7);
                    LottoGenerator generator = new LottoGenerator();
                    int purchaseAmount = 10000; // 10장 구매

                    // when
                    List<Lotto> lottos = generator.generate(10);
                    LottoResult result = new LottoResult();
                    for (Lotto lotto : lottos) {
                        Rank rank = winningNumbers.match(lotto);
                        result.addRank(rank);
                    }

                    // then
                    assertAll(
                            () -> assertThat(result.countOf(Rank.FIRST)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.SECOND)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.THIRD)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.FOURTH)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.FIFTH)).isEqualTo(0),
                            () -> assertThat(result.calculateTotalPrize()).isEqualTo(0),
                            () -> assertThat(result.calculateProfitRate(purchaseAmount)).isEqualTo(0.0)
                    );
                },
                // 모두 2개 이하 일치 (꽝)
                List.of(10, 11, 12, 13, 14, 15),
                List.of(20, 21, 22, 23, 24, 25),
                List.of(1, 2, 30, 31, 32, 33),   // 2개 일치 (꽝)
                List.of(3, 4, 34, 35, 36, 37),   // 2개 일치 (꽝)
                List.of(5, 38, 39, 40, 41, 42),  // 1개 일치 (꽝)
                List.of(6, 15, 16, 17, 18, 19),  // 1개 일치 (꽝)
                List.of(25, 26, 27, 28, 29, 30),
                List.of(31, 32, 33, 34, 35, 36),
                List.of(37, 38, 39, 40, 41, 42),
                List.of(8, 9, 10, 11, 12, 13)
        );
    }

    @DisplayName("실전 시나리오 - 5등 많고, 4등 약간, 나머지 꽝")
    @Test
    void 실전_시나리오() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // given
                    WinningNumbers winningNumbers = new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7);
                    LottoGenerator generator = new LottoGenerator();
                    int purchaseAmount = 10000; // 10장 구매

                    // when
                    List<Lotto> lottos = generator.generate(10);
                    LottoResult result = new LottoResult();
                    for (Lotto lotto : lottos) {
                        Rank rank = winningNumbers.match(lotto);
                        result.addRank(rank);
                    }

                    // then
                    long expectedTotalPrize = (long) Rank.FIFTH.getPrize() * 5 + (long) Rank.FOURTH.getPrize() * 2;
                    double expectedProfitRate = (double) expectedTotalPrize / purchaseAmount * 100;

                    assertAll(
                            () -> assertThat(result.countOf(Rank.FIRST)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.SECOND)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.THIRD)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.FOURTH)).isEqualTo(2),
                            () -> assertThat(result.countOf(Rank.FIFTH)).isEqualTo(5),
                            () -> assertThat(result.calculateTotalPrize()).isEqualTo(expectedTotalPrize),
                            () -> assertThat(result.calculateProfitRate(purchaseAmount))
                                    .isCloseTo(expectedProfitRate, within(0.01)),
                            // 수익률이 1250%인지 추가 검증
                            () -> assertThat(result.calculateProfitRate(purchaseAmount))
                                    .isCloseTo(1250.0, within(0.01))
                    );
                },
                // 5등 5개
                List.of(1, 2, 3, 15, 16, 17),
                List.of(1, 2, 4, 18, 19, 20),
                List.of(1, 3, 5, 21, 22, 23),
                List.of(2, 4, 6, 24, 25, 26),
                List.of(3, 5, 6, 27, 28, 29),
                // 4등 2개
                List.of(1, 2, 3, 4, 30, 31),
                List.of(2, 3, 5, 6, 32, 33),
                // 꽝 3개
                List.of(1, 2, 34, 35, 36, 37),   // 2개 일치
                List.of(10, 11, 12, 13, 14, 15),
                List.of(20, 21, 22, 23, 24, 25)
        );
    }

    @DisplayName("2등 특수 케이스 - 5개 일치 + 보너스 일치")
    @Test
    void 이등_특수_케이스() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // given
                    WinningNumbers winningNumbers = new WinningNumbers(List.of(10, 20, 30, 40, 41, 42), 45);
                    LottoGenerator generator = new LottoGenerator();
                    int purchaseAmount = 3000; // 3장 구매

                    // when
                    List<Lotto> lottos = generator.generate(3);
                    LottoResult result = new LottoResult();
                    for (Lotto lotto : lottos) {
                        Rank rank = winningNumbers.match(lotto);
                        result.addRank(rank);
                    }

                    // then
                    long expectedTotalPrize = (long) Rank.SECOND.getPrize() * 2 + Rank.THIRD.getPrize();
                    double expectedProfitRate = (double) expectedTotalPrize / purchaseAmount * 100;

                    assertAll(
                            () -> assertThat(result.countOf(Rank.FIRST)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.SECOND)).isEqualTo(2),
                            () -> assertThat(result.countOf(Rank.THIRD)).isEqualTo(1),
                            () -> assertThat(result.countOf(Rank.FOURTH)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.FIFTH)).isEqualTo(0),
                            () -> assertThat(result.calculateTotalPrize()).isEqualTo(expectedTotalPrize),
                            () -> assertThat(result.calculateProfitRate(purchaseAmount))
                                    .isCloseTo(expectedProfitRate, within(0.01))
                    );
                },
                // 당첨 번호: 10,20,30,40,41,42 / 보너스: 45
                List.of(10, 20, 30, 40, 41, 45),  // 2등 (5개 일치 + 보너스)
                List.of(10, 20, 30, 40, 42, 45),  // 2등 (5개 일치 + 보너스)
                List.of(10, 20, 30, 40, 41, 1)    // 3등 (5개 일치, 보너스X)
        );
    }

    @DisplayName("복합 시나리오 - 고액 당첨과 소액 당첨 섞여있는 경우")
    @Test
    void 복합_시나리오() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // given
                    WinningNumbers winningNumbers = new WinningNumbers(List.of(5, 10, 15, 20, 25, 30), 35);
                    LottoGenerator generator = new LottoGenerator();
                    int purchaseAmount = 8000; // 8장 구매

                    // when
                    List<Lotto> lottos = generator.generate(8);
                    LottoResult result = new LottoResult();
                    for (Lotto lotto : lottos) {
                        Rank rank = winningNumbers.match(lotto);
                        result.addRank(rank);
                    }

                    // then
                    long expectedTotalPrize = Rank.FIRST.getPrize()
                            + Rank.THIRD.getPrize()
                            + (long) Rank.FOURTH.getPrize() * 2
                            + (long) Rank.FIFTH.getPrize() * 3;
                    double expectedProfitRate = (double) expectedTotalPrize / purchaseAmount * 100;

                    assertAll(
                            () -> assertThat(result.countOf(Rank.FIRST)).isEqualTo(1),
                            () -> assertThat(result.countOf(Rank.SECOND)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.THIRD)).isEqualTo(1),
                            () -> assertThat(result.countOf(Rank.FOURTH)).isEqualTo(2),
                            () -> assertThat(result.countOf(Rank.FIFTH)).isEqualTo(3),
                            () -> assertThat(result.calculateTotalPrize()).isEqualTo(expectedTotalPrize),
                            () -> assertThat(result.calculateProfitRate(purchaseAmount))
                                    .isCloseTo(expectedProfitRate, within(0.01))
                    );
                },
                // 당첨 번호: 5,10,15,20,25,30 / 보너스: 35
                List.of(5, 10, 15, 20, 25, 30),   // 1등
                List.of(5, 10, 15, 20, 25, 1),    // 3등 (5개 일치, 보너스X)
                List.of(5, 10, 15, 20, 36, 37),   // 4등
                List.of(10, 15, 25, 30, 38, 39),  // 4등
                List.of(5, 10, 15, 40, 41, 42),   // 5등
                List.of(20, 25, 30, 43, 44, 45),  // 5등
                List.of(5, 20, 30, 1, 2, 3),      // 5등
                List.of(1, 2, 3, 4, 6, 7)         // 꽝
        );
    }

    @DisplayName("대량 구매 시나리오 - 20장 구매, 등수 정확히 지정")
    @Test
    void 대량_구매_시나리오() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // given
                    WinningNumbers winningNumbers = new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7);
                    LottoGenerator generator = new LottoGenerator();
                    int purchaseAmount = 20000; // 20장 구매

                    // when
                    List<Lotto> lottos = generator.generate(20);
                    LottoResult result = new LottoResult();
                    for (Lotto lotto : lottos) {
                        Rank rank = winningNumbers.match(lotto);
                        result.addRank(rank);
                    }

                    // then
                    long expectedTotalPrize = Rank.FIRST.getPrize()
                            + Rank.SECOND.getPrize()
                            + (long) Rank.THIRD.getPrize() * 2
                            + (long) Rank.FOURTH.getPrize() * 5
                            + (long) Rank.FIFTH.getPrize() * 7;
                    double expectedProfitRate = (double) expectedTotalPrize / purchaseAmount * 100;

                    assertAll(
                            () -> assertThat(result.countOf(Rank.FIRST)).isEqualTo(1),
                            () -> assertThat(result.countOf(Rank.SECOND)).isEqualTo(1),
                            () -> assertThat(result.countOf(Rank.THIRD)).isEqualTo(2),
                            () -> assertThat(result.countOf(Rank.FOURTH)).isEqualTo(5),
                            () -> assertThat(result.countOf(Rank.FIFTH)).isEqualTo(7),
                            () -> assertThat(result.calculateTotalPrize()).isEqualTo(expectedTotalPrize),
                            () -> assertThat(result.calculateProfitRate(purchaseAmount))
                                    .isCloseTo(expectedProfitRate, within(0.01))
                    );
                },
                // 1등 1개
                List.of(1, 2, 3, 4, 5, 6),
                // 2등 1개
                List.of(1, 2, 3, 4, 5, 7),
                // 3등 2개
                List.of(1, 2, 3, 4, 5, 8),
                List.of(1, 2, 3, 4, 5, 9),
                // 4등 5개
                List.of(1, 2, 3, 4, 20, 30),
                List.of(1, 2, 3, 4, 21, 31),
                List.of(1, 2, 3, 4, 22, 32),
                List.of(1, 2, 3, 4, 23, 33),
                List.of(1, 2, 3, 4, 24, 34),
                // 5등 7개
                List.of(1, 2, 3, 11, 21, 31),
                List.of(1, 2, 3, 12, 22, 32),
                List.of(1, 2, 3, 13, 23, 33),
                List.of(1, 2, 3, 14, 24, 34),
                List.of(1, 2, 3, 15, 25, 35),
                List.of(1, 2, 3, 16, 26, 36),
                List.of(1, 2, 3, 17, 27, 37),
                // 꽝 4개
                List.of(10, 11, 12, 13, 14, 15),
                List.of(20, 21, 22, 23, 24, 25),
                List.of(30, 31, 32, 33, 34, 35),
                List.of(36, 37, 38, 39, 40, 41)
        );
    }

    @DisplayName("경계값 테스트 - 최소/최대 번호 조합")
    @Test
    void 경계값_테스트() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // given
                    WinningNumbers winningNumbers = new WinningNumbers(List.of(1, 2, 3, 4, 5, 6), 7);
                    LottoGenerator generator = new LottoGenerator();
                    int purchaseAmount = 3000; // 3장 구매

                    // when
                    List<Lotto> lottos = generator.generate(3);
                    LottoResult result = new LottoResult();
                    for (Lotto lotto : lottos) {
                        Rank rank = winningNumbers.match(lotto);
                        result.addRank(rank);
                    }

                    // then
                    assertAll(
                            () -> assertThat(result.countOf(Rank.FIRST)).isEqualTo(1),
                            () -> assertThat(result.countOf(Rank.SECOND)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.THIRD)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.FOURTH)).isEqualTo(0),
                            () -> assertThat(result.countOf(Rank.FIFTH)).isEqualTo(0),
                            () -> assertThat(result.calculateTotalPrize()).isEqualTo(Rank.FIRST.getPrize())
                    );
                },
                List.of(1, 2, 3, 4, 5, 6),       // 최소값 조합 (1등)
                List.of(40, 41, 42, 43, 44, 45), // 최대값 조합 (꽝)
                List.of(1, 10, 20, 30, 40, 45)   // 최소+최대 혼합 (꽝)
        );
    }

    @Override
    protected void runMain() {
        // 통합 테스트이므로 비워둠
    }
}
