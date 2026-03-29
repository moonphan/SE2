package group12.ecwms.moonpham.features.orders.services;

import group12.ecwms.moonpham.domain.entity.Order;
import group12.ecwms.moonpham.domain.enums.PaymentMethod;
import group12.ecwms.moonpham.domain.enums.ShippingMethod;

import java.util.List;

public interface OrderService {
    Order checkout(Long userId, String shippingAddress, ShippingMethod shippingMethod, PaymentMethod paymentMethod);
    void markOrderPaid(String orderCode, String paymentCode);
    List<Order> getUserOrders(Long userId);
    Order getUserOrderDetail(Long userId, String orderCode);
}

