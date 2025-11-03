package lotto.game.classicLotto.view.dto;

import java.util.List;

public record WinningStatisticsDTO(
        List<RankStatistic> rankStatistics,
        double profitRate
) {
}
