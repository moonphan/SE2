package group12.ecwms.moonpham.features.warranty.controllers;

import group12.ecwms.moonpham.common.dto.form.WarrantyTicketForm;
import group12.ecwms.moonpham.common.dto.session.SessionUser;
import group12.ecwms.moonpham.domain.entity.DigitalWarranty;
import group12.ecwms.moonpham.domain.entity.WarrantyTicket;
import group12.ecwms.moonpham.features.warranty.services.WarrantyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class WarrantyController {

    private final WarrantyService warrantyService;

    /**
     * Track warranty: load list (with optional search by serial or product name), handle load failure + empty state per activity diagram.
     */
    @GetMapping("/warranties")
    public String list(
            @RequestParam(name = "q", required = false) String q,
            HttpSession session,
            Model model
    ) {
        SessionUser user = currentUser(session);
        if (user == null) {
            return "redirect:/auth/login";
        }
        String qNorm = q == null ? "" : q;
        model.addAttribute("searchQuery", qNorm);
        model.addAttribute("hasSearchQuery", !qNorm.trim().isEmpty());
        try {
            List<DigitalWarranty> list = warrantyService.listForUser(user.id(), q);
            model.addAttribute("warranties", list);
            model.addAttribute("loadError", false);
            model.addAttribute("loadErrorMessage", null);
        } catch (Exception ex) {
            model.addAttribute("warranties", Collections.emptyList());
            model.addAttribute("loadError", true);
            model.addAttribute("loadErrorMessage", "Could not load warranties. Please try again.");
        }
        return "warranties";
    }

    @GetMapping("/warranties/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        SessionUser user = currentUser(session);
        if (user == null) {
            return "redirect:/auth/login";
        }
        Optional<DigitalWarranty> opt = warrantyService.findDetailForUser(user.id(), id);
        if (opt.isEmpty()) {
            return "redirect:/warranties";
        }
        DigitalWarranty dw = opt.get();
        List<WarrantyTicket> tickets = warrantyService.listTickets(id);
        model.addAttribute("warranty", dw);
        model.addAttribute("tickets", tickets);
        model.addAttribute("expired", warrantyService.isWarrantyExpired(dw));
        return "warranty-detail";
    }

    /**
     * Request warranty: show form only if warranty is not expired (activity diagram).
     */
    @GetMapping("/warranties/{id}/request")
    public String requestForm(@PathVariable Long id, HttpSession session, Model model) {
        SessionUser user = currentUser(session);
        if (user == null) {
            return "redirect:/auth/login";
        }
        Optional<DigitalWarranty> opt = warrantyService.findDetailForUser(user.id(), id);
        if (opt.isEmpty()) {
            return "redirect:/warranties";
        }
        DigitalWarranty dw = opt.get();
        boolean expired = warrantyService.isWarrantyExpired(dw);
        model.addAttribute("warranty", dw);
        model.addAttribute("expired", expired);
        if (!expired) {
            model.addAttribute("ticketForm", new WarrantyTicketForm());
        }
        return "warranty-request";
    }

    @PostMapping("/warranties/{id}/tickets")
    public String submitTicket(
            @PathVariable Long id,
            @ModelAttribute("ticketForm") WarrantyTicketForm form,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        SessionUser user = currentUser(session);
        if (user == null) {
            return "redirect:/auth/login";
        }
        String err = warrantyService.createSupportTicket(user.id(), id, form.getIssueType(), form.getDescription());
        if (err != null) {
            redirectAttributes.addFlashAttribute("ticketError", err);
            return "redirect:/warranties/" + id + "/request";
        }
        redirectAttributes.addFlashAttribute("ticketSuccess", "Your warranty request was submitted successfully. A support ticket has been created.");
        return "redirect:/warranties/" + id;
    }

    private SessionUser currentUser(HttpSession session) {
        Object v = session.getAttribute("currentUser");
        if (v instanceof SessionUser su) {
            return su;
        }
        return null;
    }
}
