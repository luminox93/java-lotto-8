package lotto.domain;

import java.util.Arrays;

public enum Rank {
    FIRST(6, false, 2_000_000_000),
    SECOND(5, true, 30_000_000),
    THIRD(5, false, 1_500_000),
    FOURTH(4, false, 50_000),
    FIFTH(3, false, 5_000),
    NONE(0, false, 0);

    private final int matchCount;
    private final boolean requireBonus;
    private final int prize;

    Rank(int matchCount, boolean requireBonus, int prize) {
        this.matchCount = matchCount;
        this.requireBonus = requireBonus;
        this.prize = prize;
    }

    public static Rank of(int matchCount, boolean bonusMatch) {
        return Arrays.stream(values())
                .filter(rank -> rank != NONE)
                .filter(rank -> rank.matchCount == matchCount)
                .filter(rank -> rank.requireBonus == bonusMatch)
                .findFirst()
                .orElse(NONE);
    }

    public int getMatchCount() {
        return matchCount;
    }

    public boolean isRequireBonus() {
        return requireBonus;
    }

    public int getPrize() {
        return prize;
    }

    public boolean isWinning() {
        return this != NONE;
    }
}
