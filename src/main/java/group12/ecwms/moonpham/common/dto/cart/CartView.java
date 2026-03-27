package group12.ecwms.moonpham.common.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartView(
        List<CartItemView> items,
        BigDecimal totalPrice,
        boolean canProceedToCheckout
) {
}

