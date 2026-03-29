package group12.ecwms.moonpham.features.payment;

import group12.ecwms.moonpham.domain.entity.Order;

import java.util.Map;

public interface PaymentService {
    String createVnpayPaymentUrl(Order order, String clientIp);
    boolean validateVnpayCallback(Map<String, String> params);
}

