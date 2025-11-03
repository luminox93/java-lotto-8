package lotto;

import lotto.game.classicLotto.Lotto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class LottoTest {

    @DisplayName("로또 번호 6개로 정상 생성된다")
    @Test
    void 로또_번호_6개로_정상_생성된다() {
        // given & when
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));

        // then
        assertThat(lotto.getNumbers()).hasSize(6);
    }

    @DisplayName("로또 번호의 개수가 6개가 넘어가면 예외가 발생한다")
    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호의 개수가 6개 미만이면 예외가 발생한다")
    @Test
    void 로또_번호의_개수가_6개_미만이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호가 1보다 작으면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void 로또_번호가_1보다_작으면_예외가_발생한다(int invalidNumber) {
        assertThatThrownBy(() -> new Lotto(List.of(invalidNumber, 2, 3, 4, 5, 6)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호가 45보다 크면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {46, 50, 100})
    void 로또_번호가_45보다_크면_예외가_발생한다(int invalidNumber) {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, invalidNumber)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("당첨 번호와 일치하는 개수를 정확히 반환한다 - 0개 일치")
    @Test
    void 당첨_번호와_0개_일치() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> winningNumbers = List.of(11, 12, 13, 14, 15, 16);

        // when
        int matchCount = lotto.countMatches(winningNumbers);

        // then
        assertThat(matchCount).isEqualTo(0);
    }

    @DisplayName("당첨 번호와 일치하는 개수를 정확히 반환한다 - 3개 일치")
    @Test
    void 당첨_번호와_3개_일치() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> winningNumbers = List.of(1, 2, 3, 14, 15, 16);

        // when
        int matchCount = lotto.countMatches(winningNumbers);

        // then
        assertThat(matchCount).isEqualTo(3);
    }

    @DisplayName("당첨 번호와 일치하는 개수를 정확히 반환한다 - 6개 모두 일치")
    @Test
    void 당첨_번호와_6개_모두_일치() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);

        // when
        int matchCount = lotto.countMatches(winningNumbers);

        // then
        assertThat(matchCount).isEqualTo(6);
    }

    @DisplayName("특정 번호가 포함되어 있는지 확인한다")
    @Test
    void 특정_번호가_포함되어_있는지_확인() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));

        // when & then
        assertAll(
                () -> assertThat(lotto.contains(1)).isTrue(),
                () -> assertThat(lotto.contains(6)).isTrue(),
                () -> assertThat(lotto.contains(7)).isFalse(),
                () -> assertThat(lotto.contains(45)).isFalse()
        );
    }

    @DisplayName("getNumbers는 방어적 복사본을 반환한다")
    @Test
    void getNumbers는_방어적_복사본을_반환한다() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));

        // when
        List<Integer> numbers = lotto.getNumbers();

        // then
        assertThatThrownBy(() -> numbers.add(7))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
