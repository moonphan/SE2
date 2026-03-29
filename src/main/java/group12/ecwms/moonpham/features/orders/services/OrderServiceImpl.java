package group12.ecwms.moonpham.features.orders.services;

import group12.ecwms.moonpham.domain.entity.Cart;
import group12.ecwms.moonpham.domain.entity.CartItem;
import group12.ecwms.moonpham.domain.entity.DigitalWarranty;
import group12.ecwms.moonpham.domain.entity.Order;
import group12.ecwms.moonpham.domain.entity.OrderItem;
import group12.ecwms.moonpham.domain.entity.Product;
import group12.ecwms.moonpham.domain.entity.Payment;
import group12.ecwms.moonpham.domain.entity.UserAccount;
import group12.ecwms.moonpham.domain.enums.OrderStatus;
import group12.ecwms.moonpham.domain.enums.PaymentMethod;
import group12.ecwms.moonpham.domain.enums.PaymentStatus;
import group12.ecwms.moonpham.domain.enums.ShippingMethod;
import group12.ecwms.moonpham.domain.enums.WarrantyStatus;
import group12.ecwms.moonpham.common.exception.BadRequestException;
import group12.ecwms.moonpham.features.auth.repository.UserAccountRepository;
import group12.ecwms.moonpham.features.cart.repository.CartItemRepository;
import group12.ecwms.moonpham.features.cart.repository.CartRepository;
import group12.ecwms.moonpham.features.orders.repository.OrderItemRepository;
import group12.ecwms.moonpham.features.orders.repository.OrderRepository;
import group12.ecwms.moonpham.features.orders.repository.PaymentRepository;
import group12.ecwms.moonpham.features.warranty.repository.DigitalWarrantyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserAccountRepository userAccountRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final DigitalWarrantyRepository digitalWarrantyRepository;

    @Override
    @Transactional
    public Order checkout(Long userId, String shippingAddress, ShippingMethod shippingMethod, PaymentMethod paymentMethod) {
        if (shippingAddress == null || shippingAddress.isBlank()) {
            throw new BadRequestException("Shipping address is required");
        }

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new BadRequestException("Cart is empty"));
        List<CartItem> items = cartItemRepository.findByCart_Id(cart.getId());
        if (items.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : items) {
            Product product = item.getProduct();
            int stock = product.getStockQuantity() == null ? 0 : product.getStockQuantity();
            if (stock <= 0) {
                throw new BadRequestException("Item " + product.getName() + " is out of stock");
            }
            if (item.getQuantity() > stock) {
                throw new BadRequestException("Only " + stock + " items left for " + product.getName());
            }
            // Use service package price (CartItem.unitPrice) if available
            BigDecimal unit = item.getUnitPrice() == null ? product.getUnitPrice() : item.getUnitPrice();
            subtotal = subtotal.add(unit.multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        BigDecimal shippingFee = shippingMethod == ShippingMethod.express ? BigDecimal.valueOf(30000) : BigDecimal.ZERO;
        BigDecimal finalTotal = subtotal.add(shippingFee);

        Order order = new Order();
        order.setUser(userAccountRepository.getReferenceById(userId));
        order.setOrderCode("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setShippingAddress(shippingAddress.trim());
        order.setShippingMethod(shippingMethod);
        order.setPaymentMethod(paymentMethod);
        // Spec orderStatus: pending/confirmed/shipping/delivered/cancelled
        order.setOrderStatus(paymentMethod == PaymentMethod.cod ? OrderStatus.confirmed : OrderStatus.pending);
        order.setTotalAmount(subtotal);
        order.setFinalTotal(finalTotal);
        Order savedOrder = orderRepository.save(order);

        // Create Payment record (pending until VNPay callback / delivery for COD)
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setMethod(paymentMethod);
        payment.setStatus(PaymentStatus.pending);
        payment.setAmount(finalTotal);
        paymentRepository.save(payment);

        for (CartItem item : items) {
            Product product = item.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            // Use service package price & name if present
            if (item.getServicePackage() != null) {
                orderItem.setUnitPrice(item.getServicePackage().getPrice());
                orderItem.setServicePackageName(item.getServicePackage().getName());
                orderItem.setSubtotal(item.getServicePackage().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            } else {
                orderItem.setUnitPrice(product.getUnitPrice());
                orderItem.setServicePackageName(null);
                orderItem.setSubtotal(product.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
            OrderItem savedItem = orderItemRepository.save(orderItem);
            createDigitalWarrantyIfAbsent(savedOrder.getUser(), savedItem, product);

            // SRS checkout lock/hold stock simplified: deduct at checkout creation
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
        }

        cartItemRepository.deleteAll(items);
        return savedOrder;
    }

    private void createDigitalWarrantyIfAbsent(UserAccount user, OrderItem orderItem, Product product) {
        if (digitalWarrantyRepository.existsByOrderItem_Id(orderItem.getId())) {
            return;
        }
        int months = product.getWarrantyPeriodMonths() == null ? 12 : product.getWarrantyPeriodMonths();
        LocalDate start = LocalDate.now();
        DigitalWarranty dw = new DigitalWarranty();
        dw.setUser(user);
        dw.setOrderItem(orderItem);
        dw.setProduct(product);
        dw.setSerialNumber("DW-" + orderItem.getId() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        dw.setWarrantyStartDate(start);
        dw.setWarrantyEndDate(start.plusMonths(months));
        dw.setWarrantyStatus(WarrantyStatus.active);
        digitalWarrantyRepository.save(dw);
    }

    @Override
    @Transactional
    public void markOrderPaid(String orderCode, String paymentCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new BadRequestException("Order not found"));
        order.setOrderStatus(OrderStatus.confirmed);
        order.setPaymentCode(paymentCode);
        order.setPaidAt(LocalDateTime.now());
        orderRepository.save(order);

        paymentRepository.findByOrder_OrderCode(orderCode).ifPresent(p -> {
            p.setStatus(PaymentStatus.paid);
            paymentRepository.save(p);
        });
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUser_IdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Order getUserOrderDetail(Long userId, String orderCode) {
        return orderRepository.findByOrderCodeAndUser_Id(orderCode, userId)
                .orElseThrow(() -> new BadRequestException("Order not found"));
    }
}

