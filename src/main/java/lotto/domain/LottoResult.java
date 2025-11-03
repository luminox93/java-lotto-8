package lotto.domain;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class LottoResult {
    private final Map<Rank, Integer> result;

    public LottoResult() {
        this.result = new EnumMap<>(Rank.class);
        initializeResult();
    }

    private void initializeResult() {
        Arrays.stream(Rank.values())
                .filter(Rank::isWinning)
                .forEach(rank -> result.put(rank, 0));
    }

    public void addRank(Rank rank) {
        if (rank.isWinning()) {
            result.put(rank, result.get(rank) + 1);
        }
    }

    public int countOf(Rank rank) {
        return result.getOrDefault(rank, 0);
    }

    public long calculateTotalPrize() {
        return result.entrySet().stream()
                .mapToLong(entry -> (long) entry.getKey().getPrize() * entry.getValue())
                .sum();
    }

    public double calculateProfitRate(int purchaseAmount) {
        long totalPrize = calculateTotalPrize();
        return (double) totalPrize / purchaseAmount * 100;
    }
}
