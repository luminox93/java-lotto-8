package lotto;

import lotto.common.exception.InvalidInputException;
import lotto.common.exception.InvalidPurchaseAmountException;
import lotto.game.classicLotto.InputParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class InputParserTest {

    private InputParser parser;

    @BeforeEach
    void setUp() {
        parser = new InputParser();
    }

    // ========== 구매 금액 파싱 테스트 ==========

    @DisplayName("정상적인 구매 금액을 파싱한다")
    @ParameterizedTest
    @ValueSource(strings = {"1000", "5000", "10000", "100000"})
    void 정상적인_구매금액_파싱(String input) {
        // when
        int amount = parser.parsePurchaseAmount(input);

        // then
        assertThat(amount).isPositive();
        assertThat(amount % 1000).isEqualTo(0);
    }

    @DisplayName("구매 금액에 공백이 있어도 파싱한다")
    @Test
    void 구매금액_공백_포함() {
        // when & then
        assertAll(
                () -> assertThat(parser.parsePurchaseAmount(" 5000 ")).isEqualTo(5000),
                () -> assertThat(parser.parsePurchaseAmount("  10000  ")).isEqualTo(10000),
                () -> assertThat(parser.parsePurchaseAmount("\t8000\t")).isEqualTo(8000)
        );
    }

    @DisplayName("빈 문자열이나 공백만 있으면 예외를 발생시킨다")
    @Test
    void 구매금액_빈문자열_또는_공백() {
        assertAll(
                () -> assertThatThrownBy(() -> parser.parsePurchaseAmount(""))
                        .isInstanceOf(InvalidPurchaseAmountException.class),
                () -> assertThatThrownBy(() -> parser.parsePurchaseAmount("   "))
                        .isInstanceOf(InvalidPurchaseAmountException.class),
                () -> assertThatThrownBy(() -> parser.parsePurchaseAmount("\t"))
                        .isInstanceOf(InvalidPurchaseAmountException.class),
                () -> assertThatThrownBy(() -> parser.parsePurchaseAmount("\n"))
                        .isInstanceOf(InvalidPurchaseAmountException.class)
        );
    }

    @DisplayName("null이면 예외를 발생시킨다")
    @Test
    void 구매금액_null() {
        assertThatThrownBy(() -> parser.parsePurchaseAmount(null))
                .isInstanceOf(Exception.class);
    }

    @DisplayName("숫자가 아닌 값은 예외를 발생시킨다")
    @ParameterizedTest
    @ValueSource(strings = {"abc", "12a34", "천원", "1,000", "1.5"})
    void 구매금액_숫자아님(String input) {
        assertThatThrownBy(() -> parser.parsePurchaseAmount(input))
                .isInstanceOf(InvalidPurchaseAmountException.class);
    }

    @DisplayName("0 이하의 금액은 예외를 발생시킨다")
    @ParameterizedTest
    @ValueSource(strings = {"0", "-1000", "-500"})
    void 구매금액_0이하(String input) {
        assertThatThrownBy(() -> parser.parsePurchaseAmount(input))
                .isInstanceOf(InvalidPurchaseAmountException.class);
    }

    @DisplayName("1000으로 나누어떨어지지 않는 금액은 예외를 발생시킨다")
    @ParameterizedTest
    @ValueSource(strings = {"1", "500", "1500", "999", "10001"})
    void 구매금액_1000원_단위_아님(String input) {
        assertThatThrownBy(() -> parser.parsePurchaseAmount(input))
                .isInstanceOf(InvalidPurchaseAmountException.class);
    }

    // ========== 당첨 번호 파싱 테스트 ==========

    @DisplayName("정상적인 당첨 번호를 파싱한다")
    @Test
    void 정상적인_당첨번호_파싱() {
        // when
        List<Integer> numbers = parser.parseWinningNumbers("1,2,3,4,5,6");

        // then
        assertThat(numbers).containsExactly(1, 2, 3, 4, 5, 6);
    }

    @DisplayName("공백이 포함된 당첨 번호를 파싱한다")
    @Test
    void 당첨번호_공백_포함() {
        // when & then
        assertAll(
                () -> assertThat(parser.parseWinningNumbers("1, 2, 3, 4, 5, 6"))
                        .containsExactly(1, 2, 3, 4, 5, 6),
                () -> assertThat(parser.parseWinningNumbers(" 1 , 2 , 3 , 4 , 5 , 6 "))
                        .containsExactly(1, 2, 3, 4, 5, 6),
                () -> assertThat(parser.parseWinningNumbers("10,  20,  30,  40,  41,  42"))
                        .containsExactly(10, 20, 30, 40, 41, 42)
        );
    }

    @DisplayName("연속된 쉼표가 있으면 예외를 발생시킨다")
    @Test
    void 당첨번호_연속된_쉼표() {
        // when & then
        assertThatThrownBy(() -> parser.parseWinningNumbers("1,2,3,4,,5,6"))
                .isInstanceOf(InvalidInputException.class);
    }

    @DisplayName("끝에 쉼표가 있으면 예외를 발생시킨다")
    @Test
    void 당첨번호_끝에_쉼표() {
        // when & then
        assertThatThrownBy(() -> parser.parseWinningNumbers("1,2,3,4,5,6,"))
                .isInstanceOf(InvalidInputException.class);
    }

    @DisplayName("앞에 쉼표가 있으면 예외를 발생시킨다")
    @Test
    void 당첨번호_앞에_쉼표() {
        // when & then
        assertThatThrownBy(() -> parser.parseWinningNumbers(",1,2,3,4,5,6"))
                .isInstanceOf(InvalidInputException.class);
    }

    @DisplayName("숫자가 아닌 값이 포함되면 예외를 발생시킨다")
    @ParameterizedTest
    @ValueSource(strings = {
            "1,a,3,4,5,6",
            "1,2,삼,4,5,6",
            "1,2,3,four,5,6",
            "1,2.5,3,4,5,6"
    })
    void 당첨번호_숫자아님(String input) {
        assertThatThrownBy(() -> parser.parseWinningNumbers(input))
                .isInstanceOf(Exception.class);
    }

    @DisplayName("빈 문자열이나 공백만 있으면 예외를 발생시킨다")
    @Test
    void 당첨번호_빈문자열_또는_공백() {
        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> parser.parseWinningNumbers(""))
                        .isInstanceOf(InvalidInputException.class),
                () -> assertThatThrownBy(() -> parser.parseWinningNumbers("   "))
                        .isInstanceOf(InvalidInputException.class),
                () -> assertThatThrownBy(() -> parser.parseWinningNumbers("\t"))
                        .isInstanceOf(InvalidInputException.class),
                () -> assertThatThrownBy(() -> parser.parseWinningNumbers("\n"))
                        .isInstanceOf(InvalidInputException.class)
        );
    }

    @DisplayName("null이면 예외를 발생시킨다")
    @Test
    void 당첨번호_null() {
        assertThatThrownBy(() -> parser.parseWinningNumbers(null))
                .isInstanceOf(InvalidInputException.class);
    }

    @DisplayName("개수가 6개가 아닌 경우도 파싱은 된다 (Lotto 생성 시 검증)")
    @Test
    void 당첨번호_개수_다름() {
        // when & then (파싱은 성공하지만 개수는 다름)
        assertAll(
                () -> assertThat(parser.parseWinningNumbers("1,2,3")).hasSize(3),
                () -> assertThat(parser.parseWinningNumbers("1,2,3,4,5,6,7")).hasSize(7),
                () -> assertThat(parser.parseWinningNumbers("1")).hasSize(1),
                () -> assertThat(parser.parseWinningNumbers("1,2,3,4,5")).hasSize(5)
        );
    }

    @DisplayName("범위를 벗어난 숫자도 파싱은 된다 (Lotto/WinningNumbers 생성 시 검증)")
    @Test
    void 당첨번호_범위_벗어남() {
        // when & then (파싱은 성공)
        assertAll(
                // 45보다 큰 숫자
                () -> assertThat(parser.parseWinningNumbers("1,2,3,4,5,46"))
                        .containsExactly(1, 2, 3, 4, 5, 46),
                () -> assertThat(parser.parseWinningNumbers("50,51,52,53,54,55"))
                        .containsExactly(50, 51, 52, 53, 54, 55),
                () -> assertThat(parser.parseWinningNumbers("1,2,3,4,5,100"))
                        .containsExactly(1, 2, 3, 4, 5, 100),

                // 0 또는 음수
                () -> assertThat(parser.parseWinningNumbers("0,1,2,3,4,5"))
                        .containsExactly(0, 1, 2, 3, 4, 5),
                () -> assertThat(parser.parseWinningNumbers("-1,2,3,4,5,6"))
                        .containsExactly(-1, 2, 3, 4, 5, 6),
                () -> assertThat(parser.parseWinningNumbers("-10,-5,0,5,10,15"))
                        .containsExactly(-10, -5, 0, 5, 10, 15)
        );
    }

    @DisplayName("중복된 숫자도 파싱은 된다 (Lotto 생성 시 검증)")
    @Test
    void 당첨번호_중복() {
        // when & then (파싱은 성공)
        assertAll(
                () -> assertThat(parser.parseWinningNumbers("1,1,2,3,4,5"))
                        .containsExactly(1, 1, 2, 3, 4, 5),
                () -> assertThat(parser.parseWinningNumbers("1,2,3,3,3,4"))
                        .containsExactly(1, 2, 3, 3, 3, 4),
                () -> assertThat(parser.parseWinningNumbers("5,5,5,5,5,5"))
                        .containsExactly(5, 5, 5, 5, 5, 5)
        );
    }

    // ========== 보너스 번호 파싱 테스트 ==========

    @DisplayName("정상적인 보너스 번호를 파싱한다")
    @ParameterizedTest
    @ValueSource(strings = {"7", "45", "1", "23"})
    void 정상적인_보너스번호_파싱(String input) {
        // when
        int bonusNumber = parser.parseBonusNumber(input);

        // then
        assertThat(bonusNumber).isPositive();
    }

    @DisplayName("보너스 번호에 공백이 있어도 파싱한다")
    @Test
    void 보너스번호_공백_포함() {
        // when & then
        assertAll(
                () -> assertThat(parser.parseBonusNumber(" 7 ")).isEqualTo(7),
                () -> assertThat(parser.parseBonusNumber("  45  ")).isEqualTo(45),
                () -> assertThat(parser.parseBonusNumber("\t10\t")).isEqualTo(10)
        );
    }

    @DisplayName("빈 문자열이나 공백만 있으면 예외를 발생시킨다")
    @Test
    void 보너스번호_빈문자열_또는_공백() {
        assertAll(
                () -> assertThatThrownBy(() -> parser.parseBonusNumber(""))
                        .isInstanceOf(Exception.class),
                () -> assertThatThrownBy(() -> parser.parseBonusNumber("   "))
                        .isInstanceOf(Exception.class),
                () -> assertThatThrownBy(() -> parser.parseBonusNumber("\t"))
                        .isInstanceOf(Exception.class),
                () -> assertThatThrownBy(() -> parser.parseBonusNumber("\n"))
                        .isInstanceOf(Exception.class)
        );
    }

    @DisplayName("null이면 예외를 발생시킨다")
    @Test
    void 보너스번호_null() {
        assertThatThrownBy(() -> parser.parseBonusNumber(null))
                .isInstanceOf(Exception.class);
    }

    @DisplayName("숫자가 아닌 값은 예외를 발생시킨다")
    @ParameterizedTest
    @ValueSource(strings = {"abc", "일곱", "7a", "3.14", "1,2"})
    void 보너스번호_숫자아님(String input) {
        assertThatThrownBy(() -> parser.parseBonusNumber(input))
                .isInstanceOf(Exception.class);
    }

    @DisplayName("범위를 벗어난 보너스 번호도 파싱은 된다 (WinningNumbers 생성 시 검증)")
    @Test
    void 보너스번호_범위_벗어남() {
        // when & then (파싱은 성공)
        assertAll(
                () -> assertThat(parser.parseBonusNumber("0")).isEqualTo(0),
                () -> assertThat(parser.parseBonusNumber("-1")).isEqualTo(-1),
                () -> assertThat(parser.parseBonusNumber("46")).isEqualTo(46),
                () -> assertThat(parser.parseBonusNumber("100")).isEqualTo(100),
                () -> assertThat(parser.parseBonusNumber("-999")).isEqualTo(-999)
        );
    }

    // ========== 복합 시나리오 테스트 ==========

    @DisplayName("다양한 잘못된 형식을 모두 감지한다")
    @Test
    void 복합_에러_케이스() {
        assertAll(
                // 구매 금액 에러
                () -> assertThatThrownBy(() -> parser.parsePurchaseAmount("천원"))
                        .isInstanceOf(InvalidPurchaseAmountException.class),
                () -> assertThatThrownBy(() -> parser.parsePurchaseAmount("1500"))
                        .isInstanceOf(InvalidPurchaseAmountException.class),

                // 당첨 번호 에러
                () -> assertThatThrownBy(() -> parser.parseWinningNumbers("1,2,3,4,,5,6"))
                        .isInstanceOf(InvalidInputException.class),
                () -> assertThatThrownBy(() -> parser.parseWinningNumbers("1,2,3,4,5,6,"))
                        .isInstanceOf(InvalidInputException.class),

                // 보너스 번호 에러
                () -> assertThatThrownBy(() -> parser.parseBonusNumber(""))
                        .isInstanceOf(Exception.class),
                () -> assertThatThrownBy(() -> parser.parseBonusNumber("abc"))
                        .isInstanceOf(Exception.class)
        );
    }

    @DisplayName("실제 사용자 입력 시나리오 - 정상 케이스")
    @Test
    void 실제_사용자_입력_정상() {
        // given
        String purchaseInput = "8000";
        String winningInput = "1, 2, 3, 4, 5, 6";
        String bonusInput = "7";

        // when
        int purchaseAmount = parser.parsePurchaseAmount(purchaseInput);
        List<Integer> winningNumbers = parser.parseWinningNumbers(winningInput);
        int bonusNumber = parser.parseBonusNumber(bonusInput);

        // then
        assertAll(
                () -> assertThat(purchaseAmount).isEqualTo(8000),
                () -> assertThat(winningNumbers).containsExactly(1, 2, 3, 4, 5, 6),
                () -> assertThat(bonusNumber).isEqualTo(7)
        );
    }

    @DisplayName("실제 사용자 입력 시나리오 - 에러 케이스")
    @Test
    void 실제_사용자_입력_에러() {
        assertAll(
                // 구매 금액에 쉼표 포함
                () -> assertThatThrownBy(() -> parser.parsePurchaseAmount("8,000"))
                        .isInstanceOf(InvalidPurchaseAmountException.class),

                // 당첨 번호 연속 쉼표
                () -> assertThatThrownBy(() -> parser.parseWinningNumbers("1,2,3,4,,5,6"))
                        .isInstanceOf(InvalidInputException.class),

                // 당첨 번호 끝 쉼표
                () -> assertThatThrownBy(() -> parser.parseWinningNumbers("1,2,3,4,5,6,"))
                        .isInstanceOf(InvalidInputException.class),

                // 보너스 번호 공백만
                () -> assertThatThrownBy(() -> parser.parseBonusNumber("   "))
                        .isInstanceOf(Exception.class)
        );
    }
}
