package lotto;

import lotto.game.classicLotto.domain.LottoGenerator;
import lotto.game.classicLotto.domain.LottoResult;
import lotto.game.classicLotto.domain.Rank;
import lotto.game.classicLotto.domain.WinningNumbers;
import lotto.game.classicLotto.view.InputView;
import lotto.game.classicLotto.view.OutputView;
import lotto.game.classicLotto.view.dto.RankStatistic;
import lotto.game.classicLotto.view.dto.TicketPurchaseDTO;
import lotto.game.classicLotto.view.dto.WinningStatisticsDTO;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import lotto.game.Game;
import lotto.game.classicLotto.Lotto;

public class LottoGame implements Game {
    private static final int LOTTO_PRICE = 1000;

    private final InputView inputView;
    private final OutputView outputView;
    private final InputParser inputParser;
    private final LottoGenerator lottoGenerator;

    public LottoGame() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.inputParser = new InputParser();
        this.lottoGenerator = new LottoGenerator();
    }

    public void play() {
        int purchaseAmount = readPurchaseAmount();
        List<Lotto> lottos = generateLottos(purchaseAmount);
        printLottos(lottos);

        WinningNumbers winningNumbers = readWinningNumbers();
        LottoResult result = checkWinning(lottos, winningNumbers);
        printResult(result, purchaseAmount);
    }

    private int readPurchaseAmount() {
        return repeatUntilValid(() -> {
            String input = inputView.readPurchaseAmount();
            return inputParser.parsePurchaseAmount(input);
        });
    }

    private List<Lotto> generateLottos(int purchaseAmount) {
        int count = purchaseAmount / LOTTO_PRICE;
        return lottoGenerator.generate(count);
    }

    private void printLottos(List<Lotto> lottos) {
        List<List<Integer>> sortedTickets = lottos.stream()
                .map(Lotto::getNumbers)
                .map(numbers -> numbers.stream().sorted().toList())
                .toList();

        TicketPurchaseDTO dto = new TicketPurchaseDTO(lottos.size(), sortedTickets);
        outputView.printTicketPurchase(dto);
    }

    private WinningNumbers readWinningNumbers() {
        List<Integer> winningNumbers = readWinningNumbersList();
        int bonusNumber = readBonusNumber(winningNumbers);
        return new WinningNumbers(winningNumbers, bonusNumber);
    }

    private List<Integer> readWinningNumbersList() {
        return repeatUntilValid(() -> {
            String input = inputView.readWinningNumbers();
            List<Integer> numbers = inputParser.parseWinningNumbers(input);
            new Lotto(numbers);
            return numbers;
        });
    }

    private int readBonusNumber(List<Integer> winningNumbers) {
        return repeatUntilValid(() -> {
            String input = inputView.readBonusNumber();
            int bonusNumber = inputParser.parseBonusNumber(input);
            new WinningNumbers(winningNumbers, bonusNumber);
            return bonusNumber;
        });
    }

    private LottoResult checkWinning(List<Lotto> lottos, WinningNumbers winningNumbers) {
        LottoResult result = new LottoResult();
        for (Lotto lotto : lottos) {
            Rank rank = winningNumbers.match(lotto);
            result.addRank(rank);
        }
        return result;
    }

    private void printResult(LottoResult result, int purchaseAmount) {
        List<RankStatistic> rankStatistics = createRankStatistics(result);
        double profitRate = result.calculateProfitRate(purchaseAmount);

        WinningStatisticsDTO dto = new WinningStatisticsDTO(rankStatistics, profitRate);
        outputView.printWinningStatistics(dto);
    }

    private List<RankStatistic> createRankStatistics(LottoResult result) {
        return Rank.getWinningRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getMatchCount))
                .map(rank -> new RankStatistic(formatRankDescription(rank), result.countOf(rank)))
                .toList();
    }

    private String formatRankDescription(Rank rank) {
        String matchInfo = rank.getMatchCount() + "개 일치";
        if (rank.isRequireBonus()) {
            matchInfo += ", 보너스 볼 일치";
        }
        return String.format("%s (%,d원)", matchInfo, rank.getPrize());
    }

    private <T> T repeatUntilValid(Supplier<T> action) {
        while (true) {
            try {
                return action.get();
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }
}
