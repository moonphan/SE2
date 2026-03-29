package group12.ecwms.moonpham.features.cart.services;

import group12.ecwms.moonpham.common.dto.cart.CartItemView;
import group12.ecwms.moonpham.common.dto.cart.CartView;
import group12.ecwms.moonpham.domain.entity.Cart;
import group12.ecwms.moonpham.domain.entity.CartItem;
import group12.ecwms.moonpham.domain.entity.Product;
import group12.ecwms.moonpham.common.exception.BadRequestException;
import group12.ecwms.moonpham.features.auth.repository.UserAccountRepository;
import group12.ecwms.moonpham.features.cart.repository.CartItemRepository;
import group12.ecwms.moonpham.features.cart.repository.CartRepository;
import group12.ecwms.moonpham.features.products.repository.ProductRepository;
import group12.ecwms.moonpham.features.products.repository.ServicePackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ServicePackageRepository servicePackageRepository;
    private final UserAccountRepository userAccountRepository;

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(userAccountRepository.getReferenceById(userId));
                    cart.setCreatedAt(LocalDateTime.now());
                    return cartRepository.save(cart);
                });
    }

    @Override
    public void addToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product not found"));

        int stock = product.getStockQuantity() == null ? 0 : product.getStockQuantity();
        if (stock <= 0) {
            throw new BadRequestException("Item is out of stock");
        }
        if (quantity > stock) {
            throw new BadRequestException("Only " + stock + " items left in stock");
        }

        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setUserId(userId);
                    newItem.setQuantity(0);
                    var pkg = servicePackageRepository.findFirstByProduct_IdOrderByPriceAsc(productId)
                            .orElse(null);
                    newItem.setServicePackage(pkg);
                    newItem.setUnitPrice(pkg != null ? pkg.getPrice() : product.getUnitPrice());
                    return newItem;
                });

        int newQty = item.getQuantity() + quantity;
        if (newQty > stock) {
            throw new BadRequestException("Only " + stock + " items left in stock");
        }

        item.setQuantity(newQty);
        // Keep unitPrice tied to service package price
        if (item.getServicePackage() != null) {
            item.setUnitPrice(item.getServicePackage().getPrice());
        } else {
            item.setUnitPrice(product.getUnitPrice());
        }
        cartItemRepository.save(item);
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCart_IdAndProduct_Id(cart.getId(), productId);
    }

    @Override
    public void updateCartQuantities(Long userId, List<Long> productIds, List<Integer> quantities) {
        if (productIds == null || quantities == null || productIds.size() != quantities.size()) {
            throw new BadRequestException("Invalid update request");
        }

        Cart cart = getOrCreateCart(userId);

        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            Integer qty = quantities.get(i);
            if (productId == null || qty == null) continue;

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new BadRequestException("Product not found"));

            int stock = product.getStockQuantity() == null ? 0 : product.getStockQuantity();
            if (qty <= 0) {
                cartItemRepository.deleteByCart_IdAndProduct_Id(cart.getId(), productId);
                continue;
            }

            if (stock <= 0) {
                // SRS: OOS item khiến Proceed disabled, user phải xóa OOS item.
                // Khi update cart, ta bỏ qua update số lượng cho OOS item để tránh chặn thao tác khác.
                continue;
            }

            if (qty > stock) {
                throw new BadRequestException("Only " + stock + " items left in stock");
            }

            CartItem item = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId)
                    .orElseGet(() -> {
                        CartItem newItem = new CartItem();
                        newItem.setCart(cart);
                        newItem.setProduct(product);
                        newItem.setUserId(userId);
                        var pkg = servicePackageRepository.findFirstByProduct_IdOrderByPriceAsc(productId)
                                .orElse(null);
                        newItem.setServicePackage(pkg);
                        newItem.setUnitPrice(pkg != null ? pkg.getPrice() : product.getUnitPrice());
                        return newItem;
                    });

            if (item.getUserId() == null) {
                item.setUserId(userId);
            }

            item.setQuantity(qty);
            if (item.getServicePackage() != null) {
                item.setUnitPrice(item.getServicePackage().getPrice());
            } else {
                item.setUnitPrice(product.getUnitPrice());
            }
            cartItemRepository.save(item);
        }
    }

    @Override
    public CartView getCartForView(Long userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> items = cartItemRepository.findByCart_Id(cart.getId());

        List<CartItemView> views = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        boolean canProceed = !items.isEmpty();

        for (CartItem item : items) {
            Product product = item.getProduct();
            if (product == null) continue;

            int stockNow = product.getStockQuantity() == null ? 0 : product.getStockQuantity();
            boolean out = stockNow <= 0;
            if (out) canProceed = false;

            BigDecimal unitNow = item.getUnitPrice() == null ? BigDecimal.ZERO : item.getUnitPrice();
            BigDecimal lineTotal = unitNow.multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(lineTotal);

            views.add(new CartItemView(
                    product.getId(),
                    product.getName(),
                    product.getBrand(),
                    product.getThumbnailUrl(),
                    item.getQuantity(),
                    stockNow,
                    item.getUnitPrice() == null ? BigDecimal.ZERO : item.getUnitPrice(),
                    lineTotal,
                    out
            ));
        }

        return new CartView(views, total, canProceed);
    }
}

