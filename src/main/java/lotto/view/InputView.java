package lotto.view;

import camp.nextstep.edu.missionutils.Console;
import lotto.view.messages.InputMessage;

public class InputView {
    public String readPurchaseAmount() {
        System.out.println(InputMessage.PURCHASE_AMOUNT.getMessage());
        return Console.readLine();
    }

    public String readWinningNumbers() {
        System.out.println(InputMessage.WINNING_NUMBERS.getMessage());
        return Console.readLine();
    }

    public String readBonusNumber() {
        System.out.println(InputMessage.BONUS_NUMBER.getMessage());
        return Console.readLine();
    }
}
