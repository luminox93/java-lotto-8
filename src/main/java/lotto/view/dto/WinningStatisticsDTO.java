package lotto.view.dto;

import java.util.List;

public record WinningStatisticsDTO(
        List<RankStatistic> rankStatistics,
        double profitRate
) {
}
