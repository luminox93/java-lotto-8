package lotto;

import camp.nextstep.edu.missionutils.test.NsTest;
import lotto.game.classicLotto.Lotto;
import lotto.game.classicLotto.domain.LottoGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LottoGeneratorTest extends NsTest {

    @DisplayName("지정한 개수만큼 로또를 생성한다")
    @Test
    void 지정한_개수만큼_로또를_생성한다() {
        // given
        LottoGenerator generator = new LottoGenerator();
        int count = 5;

        // when
        List<Lotto> lottos = generator.generate(count);

        // then
        assertThat(lottos).hasSize(count);
    }

    @DisplayName("생성된 모든 로또는 6개의 번호를 가진다")
    @Test
    void 생성된_모든_로또는_6개의_번호를_가진다() {
        // given
        LottoGenerator generator = new LottoGenerator();

        // when
        List<Lotto> lottos = generator.generate(10);

        // then
        List<Executable> assertions = lottos.stream()
                .<Executable>map(lotto -> () -> assertThat(lotto.getNumbers()).hasSize(6))
                .toList();
        assertAll(assertions);
    }

    @DisplayName("생성된 로또의 모든 번호는 1~45 범위에 있다")
    @RepeatedTest(10)
    void 생성된_로또의_모든_번호는_1_45_범위에_있다() {
        // given
        LottoGenerator generator = new LottoGenerator();

        // when
        List<Lotto> lottos = generator.generate(5);

        // then
        lottos.forEach(lotto -> {
            List<Integer> numbers = lotto.getNumbers();
            assertAll(
                    () -> assertThat(numbers).allMatch(number -> number >= 1 && number <= 45),
                    () -> assertThat(numbers).hasSize(6),
                    () -> assertThat(numbers).doesNotHaveDuplicates()
            );
        });
    }

    @DisplayName("랜덤값을 지정하여 로또 생성 - 케이스 1")
    @Test
    void 랜덤값을_지정하여_로또_생성_케이스1() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // given
                    LottoGenerator generator = new LottoGenerator();

                    // when
                    List<Lotto> lottos = generator.generate(3);

                    // then
                    assertAll(
                            () -> assertThat(lottos).hasSize(3),
                            () -> assertThat(lottos.get(0).getNumbers()).containsExactly(1, 2, 3, 4, 5, 6),
                            () -> assertThat(lottos.get(1).getNumbers()).containsExactly(7, 8, 9, 10, 11, 12),
                            () -> assertThat(lottos.get(2).getNumbers()).containsExactly(13, 14, 15, 16, 17, 18)
                    );
                },
                List.of(1, 2, 3, 4, 5, 6),
                List.of(7, 8, 9, 10, 11, 12),
                List.of(13, 14, 15, 16, 17, 18)
        );
    }

    @DisplayName("랜덤값을 지정하여 로또 생성 - 케이스 2 (경계값)")
    @Test
    void 랜덤값을_지정하여_로또_생성_케이스2_경계값() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // given
                    LottoGenerator generator = new LottoGenerator();

                    // when
                    List<Lotto> lottos = generator.generate(2);

                    // then
                    assertAll(
                            () -> assertThat(lottos).hasSize(2),
                            // 첫 번째: 최소값들
                            () -> assertThat(lottos.get(0).getNumbers()).containsExactly(1, 2, 3, 4, 5, 6),
                            // 두 번째: 최대값들
                            () -> assertThat(lottos.get(1).getNumbers()).containsExactly(40, 41, 42, 43, 44, 45)
                    );
                },
                List.of(1, 2, 3, 4, 5, 6),
                List.of(40, 41, 42, 43, 44, 45)
        );
    }

    @DisplayName("랜덤값을 지정하여 로또 생성 - 케이스 3 (랜덤 조합)")
    @Test
    void 랜덤값을_지정하여_로또_생성_케이스3_랜덤조합() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // given
                    LottoGenerator generator = new LottoGenerator();

                    // when
                    List<Lotto> lottos = generator.generate(4);

                    // then
                    assertAll(
                            () -> assertThat(lottos).hasSize(4),
                            () -> assertThat(lottos.get(0).getNumbers()).containsExactly(8, 21, 23, 41, 42, 43),
                            () -> assertThat(lottos.get(1).getNumbers()).containsExactly(3, 5, 11, 16, 32, 38),
                            () -> assertThat(lottos.get(2).getNumbers()).containsExactly(7, 11, 16, 35, 36, 44),
                            () -> assertThat(lottos.get(3).getNumbers()).containsExactly(1, 8, 11, 31, 41, 42)
                    );
                },
                List.of(8, 21, 23, 41, 42, 43),
                List.of(3, 5, 11, 16, 32, 38),
                List.of(7, 11, 16, 35, 36, 44),
                List.of(1, 8, 11, 31, 41, 42)
        );
    }

    @DisplayName("반복 생성 시 매번 다른 번호 조합을 생성한다 (통계적 검증)")
    @Test
    void 반복_생성_시_매번_다른_번호_조합을_생성한다() {
        // given
        LottoGenerator generator = new LottoGenerator();

        // when
        List<Lotto> lottos = generator.generate(100);

        // then
        long uniqueCount = lottos.stream()
                .map(Lotto::getNumbers)
                .distinct()
                .count();

        // 100개 중 최소 95개 이상은 달라야 함 (랜덤성 검증)
        assertThat(uniqueCount).isGreaterThanOrEqualTo(95);
    }

    @Override
    protected void runMain() {
        // LottoGenerator는 단독 실행이 아니므로 비워둠
    }
}
