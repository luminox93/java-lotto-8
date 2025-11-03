package lotto.game.classicLotto.view;

import java.util.List;
import lotto.game.classicLotto.view.dto.RankStatistic;
import lotto.game.classicLotto.view.dto.TicketPurchaseDTO;
import lotto.game.classicLotto.view.dto.WinningStatisticsDTO;
import lotto.game.classicLotto.view.messages.OutputMessage;

public class OutputView {
    public void printTicketPurchase(TicketPurchaseDTO dto) {
        System.out.println(OutputMessage.PURCHASE_COUNT.format(dto.count()));
        dto.tickets().forEach(this::printTicket);
        printEmptyLine();
    }

    private void printTicket(List<Integer> numbers) {
        System.out.println(numbers);
    }

    public void printWinningStatistics(WinningStatisticsDTO dto) {
        printStatisticsHeader();
        printRankStatistics(dto);
        printProfitRate(dto.profitRate());
    }

    private void printStatisticsHeader() {
        System.out.println(OutputMessage.STATISTICS_HEADER.getMessage());
        System.out.println(OutputMessage.STATISTICS_DIVIDER.getMessage());
    }

    private void printRankStatistics(WinningStatisticsDTO dto) {
        dto.rankStatistics().forEach(this::printRankStatistic);
    }

    private void printRankStatistic(RankStatistic statistic) {
        System.out.println(
            OutputMessage.RANK_STATISTIC.format(statistic.description(), statistic.count())
        );
    }

    private void printProfitRate(double profitRate) {
        System.out.println(OutputMessage.PROFIT_RATE.format(profitRate));
    }

    public void printError(String message) {
        System.out.println(message);
    }

    public void printEmptyLine() {
        System.out.println();
    }

    public void println(String message) {
        System.out.println(message);
    }
}
