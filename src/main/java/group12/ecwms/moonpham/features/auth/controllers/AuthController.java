package group12.ecwms.moonpham.features.auth.controllers;

import group12.ecwms.moonpham.common.dto.form.DeleteAccountForm;
import group12.ecwms.moonpham.common.dto.form.EditProfileForm;
import group12.ecwms.moonpham.common.dto.form.ForgotPasswordForm;
import group12.ecwms.moonpham.common.dto.form.LoginForm;
import group12.ecwms.moonpham.common.dto.form.RegisterForm;
import group12.ecwms.moonpham.common.dto.form.ResetPasswordForm;
import group12.ecwms.moonpham.common.dto.request.LoginRequest;
import group12.ecwms.moonpham.common.dto.request.RegisterRequest;
import group12.ecwms.moonpham.common.dto.response.LoginResponse;
import group12.ecwms.moonpham.common.dto.response.RegisterResponse;
import group12.ecwms.moonpham.common.dto.session.SessionUser;
import group12.ecwms.moonpham.features.auth.services.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute RegisterForm registerForm,
            Model model
    ) {
        try {
            RegisterRequest request = new RegisterRequest(
                    registerForm.getFullName(),
                    registerForm.getUsername(),
                    registerForm.getEmail(),
                    registerForm.getPassword(),
                    registerForm.getConfirmPassword(),
                    registerForm.getPhoneNumber(),
                    registerForm.getAddress()
            );
            RegisterResponse response = authService.register(request);
            model.addAttribute("successMessage", response.message());
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("registerForm", registerForm);
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute LoginForm loginForm,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            LoginResponse response = authService.login(new LoginRequest(
                    loginForm.getLoginId(),
                    loginForm.getPassword()
            ));
            session.setAttribute("currentUser", new SessionUser(
                    response.userId(),
                    response.username(),
                    response.email(),
                    response.role()
            ));
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Login successful. Welcome, " + response.username() + "!"
            );
            return "redirect:/products";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("loginForm", loginForm);
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        model.addAttribute("forgotPasswordForm", new ForgotPasswordForm());
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@ModelAttribute ForgotPasswordForm forgotPasswordForm, Model model) {
        try {
            authService.forgotPassword(forgotPasswordForm.getEmail());
            model.addAttribute("successMessage",
                    "Check your email. We sent you a link to reset your password.");
        } catch (Exception ex) {
            // Activity diagram: email not found → stay on form and show message
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("forgotPasswordForm", forgotPasswordForm);
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(
            @RequestParam(required = false) String token,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (token == null || token.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Invalid reset link. Please enter your email again.");
            return "redirect:/auth/forgot-password";
        }
        if (!authService.isPasswordResetTokenValid(token)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Link expired. Please enter your email again and request a new link.");
            return "redirect:/auth/forgot-password";
        }
        ResetPasswordForm form = new ResetPasswordForm();
        form.setToken(token);
        model.addAttribute("resetPasswordForm", form);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @ModelAttribute ResetPasswordForm resetPasswordForm,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            authService.resetPassword(
                    resetPasswordForm.getToken(),
                    resetPasswordForm.getNewPassword(),
                    resetPasswordForm.getConfirmPassword()
            );
            redirectAttributes.addFlashAttribute("successMessage",
                    "Password reset successful. You can sign in with your new password.");
            return "redirect:/auth/login";
        } catch (Exception ex) {
            String msg = ex.getMessage() == null ? "" : ex.getMessage();
            if (msg.contains("Link expired")) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Link expired. Please enter your email again and request a new link.");
                return "redirect:/auth/forgot-password";
            }
            model.addAttribute("errorMessage", ex.getMessage());
            if (resetPasswordForm.getToken() != null
                    && authService.isPasswordResetTokenValid(resetPasswordForm.getToken())) {
                model.addAttribute("resetPasswordForm", resetPasswordForm);
                return "reset-password";
            }
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Link expired. Please enter your email again and request a new link.");
            return "redirect:/auth/forgot-password";
        }
    }

    /** Activity diagram: display current info (read-only). */
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        var user = authService.getActiveUserById(currentUser.id());
        model.addAttribute("fullName", user.getFullname());
        model.addAttribute("phoneNumber", user.getPhoneNumber());
        model.addAttribute("address", user.getAddress());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }

    /** User clicks Edit → update form. */
    @GetMapping("/profile/edit")
    public String profileEditPage(HttpSession session, Model model) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        var user = authService.getActiveUserById(currentUser.id());
        EditProfileForm profileForm = new EditProfileForm();
        profileForm.setFullName(user.getFullname());
        profileForm.setPhoneNumber(user.getPhoneNumber());
        profileForm.setAddress(user.getAddress());
        model.addAttribute("editProfileForm", profileForm);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile-edit";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @ModelAttribute EditProfileForm editProfileForm,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        try {
            authService.editProfile(
                    currentUser.id(),
                    editProfileForm.getFullName(),
                    editProfileForm.getPhoneNumber(),
                    editProfileForm.getAddress()
            );
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
            return "redirect:/auth/profile";
        } catch (DataAccessException ex) {
            model.addAttribute("errorMessage", "Database connection error. Please try again later.");
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        var user = authService.getActiveUserById(currentUser.id());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("editProfileForm", editProfileForm);
        return "profile-edit";
    }

    @GetMapping("/delete-account")
    public String deleteAccountPage(HttpSession session, Model model) {
        if (getSessionUser(session) == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("deleteAccountForm", new DeleteAccountForm());
        return "delete-account";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(
            @ModelAttribute DeleteAccountForm deleteAccountForm,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        SessionUser currentUser = getSessionUser(session);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        if (!Boolean.TRUE.equals(deleteAccountForm.getConfirmDelete())) {
            model.addAttribute("errorMessage", "Please confirm that you want to delete your account.");
            model.addAttribute("deleteAccountForm", deleteAccountForm);
            return "delete-account";
        }
        try {
            authService.deleteAccount(currentUser.id(), deleteAccountForm.getPassword());
            session.invalidate();
            redirectAttributes.addFlashAttribute("successMessage",
                    "Your account has been deleted.");
            return "redirect:/products";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("deleteAccountForm", deleteAccountForm);
            return "delete-account";
        }
    }

    private SessionUser getSessionUser(HttpSession session) {
        Object value = session.getAttribute("currentUser");
        if (value instanceof SessionUser sessionUser) {
            return sessionUser;
        }
        return null;
    }
}

