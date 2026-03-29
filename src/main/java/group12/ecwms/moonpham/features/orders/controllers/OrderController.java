package group12.ecwms.moonpham.features.orders.controllers;

import group12.ecwms.moonpham.common.dto.session.SessionUser;
import group12.ecwms.moonpham.domain.entity.Order;
import group12.ecwms.moonpham.features.orders.services.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public String listOrders(HttpSession session, Model model) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) return "redirect:/auth/login";

        model.addAttribute("orders", orderService.getUserOrders(currentUser.id()));
        return "orders";
    }

    @GetMapping("/orders/{orderCode}")
    public String orderDetail(@PathVariable String orderCode, HttpSession session, Model model) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) return "redirect:/auth/login";

        Order order = orderService.getUserOrderDetail(currentUser.id(), orderCode);
        model.addAttribute("order", order);
        return "order-detail";
    }

    private SessionUser getSessionUser(HttpSession session) {
        Object value = session.getAttribute("currentUser");
        if (value instanceof SessionUser su) return su;
        return null;
    }
}

