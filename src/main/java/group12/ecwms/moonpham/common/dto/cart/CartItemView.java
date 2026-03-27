package group12.ecwms.moonpham.common.dto.cart;

import java.math.BigDecimal;

public record CartItemView(
        Long productId,
        String name,
        String brand,
        String thumbnailUrl,
        int quantity,
        int stockQuantityNow,
        BigDecimal unitPriceNow,
        BigDecimal lineTotal,
        boolean outOfStock
) {
}

