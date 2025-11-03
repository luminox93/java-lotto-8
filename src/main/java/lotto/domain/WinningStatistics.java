package lotto.domain;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class WinningStatistics {
    private final Map<Rank, Integer> statistics;

    public WinningStatistics() {
        this.statistics = new EnumMap<>(Rank.class);
        initializeStatistics();
    }

    private void initializeStatistics() {
        Arrays.stream(Rank.values())
                .filter(Rank::isWinning)
                .forEach(rank -> statistics.put(rank, 0));
    }

    public void addResult(Rank rank) {
        if (rank.isWinning()) {
            statistics.put(rank, statistics.get(rank) + 1);
        }
    }

    public int getCount(Rank rank) {
        return statistics.getOrDefault(rank, 0);
    }

    public double calculateProfitRate(int purchaseAmount) {
        long totalPrize = statistics.entrySet().stream()
                .mapToLong(entry -> (long) entry.getKey().getPrize() * entry.getValue())
                .sum();
        return (double) totalPrize / purchaseAmount * 100;
    }

    public List<Rank> getWinningRanks() {
        return Arrays.stream(Rank.values())
                .filter(Rank::isWinning)
                .sorted((r1, r2) -> Integer.compare(r2.getMatchCount(), r1.getMatchCount()))
                .toList();
    }
}
