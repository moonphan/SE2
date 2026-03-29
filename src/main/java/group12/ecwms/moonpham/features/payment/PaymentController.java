package group12.ecwms.moonpham.features.payment;

import group12.ecwms.moonpham.features.orders.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    @GetMapping("/payment/vnpay-return")
    public String vnpayReturn(@RequestParam Map<String, String> params, Model model) {
        String orderCode = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        boolean valid = paymentService.validateVnpayCallback(params);

        if (valid && "00".equals(responseCode) && orderCode != null) {
            orderService.markOrderPaid(orderCode, params.get("vnp_TransactionNo"));
            model.addAttribute("successMessage", "Payment successful for order " + orderCode);
        } else {
            model.addAttribute("errorMessage", "Payment failed or invalid signature");
        }
        model.addAttribute("orderCode", orderCode);
        return "payment-result";
    }
}

