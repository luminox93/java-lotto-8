package lotto.game.classicLotto.view.dto;

import java.util.List;

public record TicketPurchaseDTO(
        int count,
        List<List<Integer>> tickets
) {
}
