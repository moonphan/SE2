package group12.ecwms.moonpham.features.cart.controllers;

import group12.ecwms.moonpham.common.dto.cart.CartView;
import group12.ecwms.moonpham.common.dto.session.SessionUser;
import group12.ecwms.moonpham.common.exception.BadRequestException;
import group12.ecwms.moonpham.features.cart.services.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private static final String SESSION_KEY = "currentUser";
    private final CartService cartService;

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) return "redirect:/auth/login";

        CartView cartView = cartService.getCartForView(currentUser.id());
        model.addAttribute("cartView", cartView);
        return "cart";
    }

    @PostMapping("/cart/items/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam int quantity,
            HttpSession session,
            Model model
    ) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) return "redirect:/auth/login";

        try {
            cartService.addToCart(currentUser.id(), productId, quantity);
            model.addAttribute("successMessage", "Added to cart successfully.");
        } catch (BadRequestException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Something went wrong.");
        }

        CartView cartView = cartService.getCartForView(currentUser.id());
        model.addAttribute("cartView", cartView);
        return "cart";
    }

    @PostMapping("/cart/items/remove")
    public String removeFromCart(
            @RequestParam Long productId,
            HttpSession session,
            Model model
    ) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) return "redirect:/auth/login";

        try {
            cartService.removeFromCart(currentUser.id(), productId);
            model.addAttribute("successMessage", "Removed from cart.");
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Something went wrong.");
        }

        CartView cartView = cartService.getCartForView(currentUser.id());
        model.addAttribute("cartView", cartView);
        return "cart";
    }

    @PostMapping("/cart")
    public String updateCart(
            @RequestParam List<Long> productIds,
            @RequestParam List<Integer> quantities,
            HttpSession session,
            Model model
    ) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) return "redirect:/auth/login";

        try {
            cartService.updateCartQuantities(currentUser.id(), productIds, quantities);
            model.addAttribute("successMessage", "Cart updated.");
        } catch (BadRequestException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Something went wrong.");
        }

        CartView cartView = cartService.getCartForView(currentUser.id());
        model.addAttribute("cartView", cartView);
        return "cart";
    }

    private SessionUser getSessionUser(HttpSession session) {
        Object value = session.getAttribute(SESSION_KEY);
        if (value instanceof SessionUser su) return su;
        return null;
    }
}

