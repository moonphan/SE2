package group12.ecwms.moonpham.features.cart.services;

import group12.ecwms.moonpham.common.dto.cart.CartView;

import java.util.List;

public interface CartService {
    void addToCart(Long userId, Long productId, int quantity);
    void removeFromCart(Long userId, Long productId);
    void updateCartQuantities(Long userId, List<Long> productIds, List<Integer> quantities);
    CartView getCartForView(Long userId);
}

