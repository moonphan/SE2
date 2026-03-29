package group12.ecwms.moonpham.features.payment;

import group12.ecwms.moonpham.common.dto.cart.CartView;
import group12.ecwms.moonpham.common.dto.form.CheckoutForm;
import group12.ecwms.moonpham.common.dto.session.SessionUser;
import group12.ecwms.moonpham.domain.entity.Order;
import group12.ecwms.moonpham.domain.enums.PaymentMethod;
import group12.ecwms.moonpham.domain.enums.ShippingMethod;
import group12.ecwms.moonpham.common.exception.BadRequestException;
import group12.ecwms.moonpham.features.cart.services.CartService;
import group12.ecwms.moonpham.features.orders.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) return "redirect:/auth/login";

        CartView cartView = cartService.getCartForView(currentUser.id());
        if (cartView.items().isEmpty()) return "redirect:/cart";

        model.addAttribute("cartView", cartView);
        model.addAttribute("checkoutForm", new CheckoutForm());
        return "checkout";
    }

    @PostMapping("/checkout/place")
    public String placeOrder(
            @ModelAttribute CheckoutForm checkoutForm,
            HttpSession session,
            HttpServletRequest request,
            Model model
    ) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) return "redirect:/auth/login";

        try {
            ShippingMethod shippingMethod = ShippingMethod.valueOf(checkoutForm.getShippingMethod());
            PaymentMethod paymentMethod = PaymentMethod.valueOf(checkoutForm.getPaymentMethod());
            Order order = orderService.checkout(
                    currentUser.id(),
                    checkoutForm.getShippingAddress(),
                    shippingMethod,
                    paymentMethod
            );

            if (paymentMethod == PaymentMethod.qr_transfer) {
                String paymentUrl = paymentService.createVnpayPaymentUrl(order, request.getRemoteAddr());
                return "redirect:" + paymentUrl;
            }
            return "redirect:/orders/" + order.getOrderCode();
        } catch (BadRequestException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Failed to place order");
        }

        CartView cartView = cartService.getCartForView(currentUser.id());
        model.addAttribute("cartView", cartView);
        model.addAttribute("checkoutForm", checkoutForm);
        return "checkout";
    }

    private SessionUser getSessionUser(HttpSession session) {
        Object value = session.getAttribute("currentUser");
        if (value instanceof SessionUser su) return su;
        return null;
    }
}

