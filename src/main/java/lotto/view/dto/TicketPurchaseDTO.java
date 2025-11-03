package lotto.view.dto;

import java.util.List;

public record TicketPurchaseDTO(
        int count,
        List<List<Integer>> tickets
) {
}
