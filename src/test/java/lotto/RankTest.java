package lotto;

import lotto.game.classicLotto.domain.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RankTest {

    @DisplayName("6개 일치 시 1등을 반환한다")
    @Test
    void 일치6개_1등() {
        // when
        Rank rank = Rank.of(6, false);

        // then
        assertAll(
                () -> assertThat(rank).isEqualTo(Rank.FIRST),
                () -> assertThat(rank.getPrize()).isEqualTo(2_000_000_000)
        );
    }

    @DisplayName("5개 일치 + 보너스 일치 시 2등을 반환한다")
    @Test
    void 일치5개_보너스포함_2등() {
        // when
        Rank rank = Rank.of(5, true);

        // then
        assertAll(
                () -> assertThat(rank).isEqualTo(Rank.SECOND),
                () -> assertThat(rank.getPrize()).isEqualTo(30_000_000),
                () -> assertThat(rank.isRequireBonus()).isTrue()
        );
    }

    @DisplayName("5개 일치 (보너스 X) 시 3등을 반환한다")
    @Test
    void 일치5개_보너스없음_3등() {
        // when
        Rank rank = Rank.of(5, false);

        // then
        assertAll(
                () -> assertThat(rank).isEqualTo(Rank.THIRD),
                () -> assertThat(rank.getPrize()).isEqualTo(1_500_000),
                () -> assertThat(rank.isRequireBonus()).isFalse()
        );
    }

    @DisplayName("4개 일치 시 4등을 반환한다")
    @Test
    void 일치4개_4등() {
        // when
        Rank rank = Rank.of(4, false);

        // then
        assertAll(
                () -> assertThat(rank).isEqualTo(Rank.FOURTH),
                () -> assertThat(rank.getPrize()).isEqualTo(50_000)
        );
    }

    @DisplayName("3개 일치 시 5등을 반환한다")
    @Test
    void 일치3개_5등() {
        // when
        Rank rank = Rank.of(3, false);

        // then
        assertAll(
                () -> assertThat(rank).isEqualTo(Rank.FIFTH),
                () -> assertThat(rank.getPrize()).isEqualTo(5_000)
        );
    }

    @DisplayName("0~2개 일치 시 낙첨을 반환한다")
    @ParameterizedTest
    @CsvSource({
            "0, false",
            "1, false",
            "2, false",
            "0, true",
            "1, true",
            "2, true"
    })
    void 낙첨_케이스(int matchCount, boolean bonusMatch) {
        // when
        Rank rank = Rank.of(matchCount, bonusMatch);

        // then
        assertAll(
                () -> assertThat(rank).isEqualTo(Rank.NONE),
                () -> assertThat(rank.getPrize()).isEqualTo(0),
                () -> assertThat(rank.isWinning()).isFalse()
        );
    }

    @DisplayName("4개 일치 시 보너스 false면 4등, true면 낙첨")
    @Test
    void 일치4개_보너스별_등수() {
        assertAll(
                () -> assertThat(Rank.of(4, false)).isEqualTo(Rank.FOURTH),
                () -> assertThat(Rank.of(4, true)).isEqualTo(Rank.NONE)
        );
    }

    @DisplayName("3개 일치 시 보너스 false면 5등, true면 낙첨")
    @Test
    void 일치3개_보너스별_등수() {
        assertAll(
                () -> assertThat(Rank.of(3, false)).isEqualTo(Rank.FIFTH),
                () -> assertThat(Rank.of(3, true)).isEqualTo(Rank.NONE),
                () -> assertThat(Rank.of(2, true)).isEqualTo(Rank.NONE),
                () -> assertThat(Rank.of(2, false)).isEqualTo(Rank.NONE)
        );
    }

    @DisplayName("각 등수의 상금이 정확하다")
    @Test
    void 각_등수의_상금이_정확하다() {
        assertAll(
                () -> assertThat(Rank.FIRST.getPrize()).isEqualTo(2_000_000_000),
                () -> assertThat(Rank.SECOND.getPrize()).isEqualTo(30_000_000),
                () -> assertThat(Rank.THIRD.getPrize()).isEqualTo(1_500_000),
                () -> assertThat(Rank.FOURTH.getPrize()).isEqualTo(50_000),
                () -> assertThat(Rank.FIFTH.getPrize()).isEqualTo(5_000),
                () -> assertThat(Rank.NONE.getPrize()).isEqualTo(0)
        );
    }

    @DisplayName("NONE을 제외한 모든 등수는 당첨이다")
    @Test
    void NONE을_제외한_모든_등수는_당첨이다() {
        assertAll(
                () -> assertThat(Rank.FIRST.isWinning()).isTrue(),
                () -> assertThat(Rank.SECOND.isWinning()).isTrue(),
                () -> assertThat(Rank.THIRD.isWinning()).isTrue(),
                () -> assertThat(Rank.FOURTH.isWinning()).isTrue(),
                () -> assertThat(Rank.FIFTH.isWinning()).isTrue(),
                () -> assertThat(Rank.NONE.isWinning()).isFalse()
        );
    }

    @DisplayName("getWinningRanks는 NONE을 제외한 당첨 등수만 반환한다")
    @Test
    void getWinningRanks는_당첨_등수만_반환한다() {
        // when
        List<Rank> winningRanks = Rank.getWinningRanks();

        // then
        assertAll(
                () -> assertThat(winningRanks).hasSize(5),
                () -> assertThat(winningRanks).containsExactly(
                        Rank.FIRST, Rank.SECOND, Rank.THIRD, Rank.FOURTH, Rank.FIFTH
                ),
                () -> assertThat(winningRanks).doesNotContain(Rank.NONE)
        );
    }

    @DisplayName("각 등수의 일치 개수가 정확하다")
    @Test
    void 각_등수의_일치_개수가_정확하다() {
        assertAll(
                () -> assertThat(Rank.FIRST.getMatchCount()).isEqualTo(6),
                () -> assertThat(Rank.SECOND.getMatchCount()).isEqualTo(5),
                () -> assertThat(Rank.THIRD.getMatchCount()).isEqualTo(5),
                () -> assertThat(Rank.FOURTH.getMatchCount()).isEqualTo(4),
                () -> assertThat(Rank.FIFTH.getMatchCount()).isEqualTo(3),
                () -> assertThat(Rank.NONE.getMatchCount()).isEqualTo(0)
        );
    }

    @DisplayName("2등만 보너스가 필요하다")
    @Test
    void 보너스필요_2등만() {
        assertAll(
                () -> assertThat(Rank.FIRST.isRequireBonus()).isFalse(),
                () -> assertThat(Rank.SECOND.isRequireBonus()).isTrue(),
                () -> assertThat(Rank.THIRD.isRequireBonus()).isFalse(),
                () -> assertThat(Rank.FOURTH.isRequireBonus()).isFalse(),
                () -> assertThat(Rank.FIFTH.isRequireBonus()).isFalse(),
                () -> assertThat(Rank.NONE.isRequireBonus()).isFalse()
        );
    }
}
