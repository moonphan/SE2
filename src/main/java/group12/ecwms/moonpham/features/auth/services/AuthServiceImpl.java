package group12.ecwms.moonpham.features.auth.services;

import group12.ecwms.moonpham.common.dto.request.LoginRequest;
import group12.ecwms.moonpham.common.dto.request.RegisterRequest;
import group12.ecwms.moonpham.common.dto.response.LoginResponse;
import group12.ecwms.moonpham.common.dto.response.RegisterResponse;
import group12.ecwms.moonpham.domain.entity.UserAccount;
import group12.ecwms.moonpham.domain.enums.AccountStatus;
import group12.ecwms.moonpham.common.exception.BadRequestException;
import group12.ecwms.moonpham.features.auth.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String RESET_TOKEN_KEY_PREFIX = "reset_token:";
    private static final Duration RESET_TOKEN_TTL = Duration.ofMinutes(15);

    private final UserAccountRepository userAccountRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new BadRequestException("Confirm password does not match");
        }

        if (userAccountRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new BadRequestException("Username already exists");
        }

        if (userAccountRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BadRequestException("Email already exists");
        }

        UserAccount user = new UserAccount();
        user.setFullname(request.fullName().trim());
        user.setUsername(request.username().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setPhoneNumber(request.phoneNumber().trim());
        user.setAddress(request.address() == null ? null : request.address().trim());

        UserAccount saved = userAccountRepository.save(user);
        return new RegisterResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                "Register successful"
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        if (request.loginId() == null || request.loginId().isBlank() || request.password() == null || request.password().isBlank()) {
            throw new BadRequestException("Username/email and password are required");
        }

        String loginId = request.loginId().trim();
        UserAccount user = userAccountRepository.findByUsernameIgnoreCase(loginId)
                .or(() -> userAccountRepository.findByEmailIgnoreCase(loginId))
                .orElseThrow(() -> new BadRequestException("Invalid username/email or password"));

        if (user.getStatus() != AccountStatus.active) {
            throw new BadRequestException("Account is not active");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid username/email or password");
        }

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    @Override
    public String forgotPassword(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email is required");
        }

        UserAccount user = userAccountRepository.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new BadRequestException("Email not found"));

        String token = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set(
                RESET_TOKEN_KEY_PREFIX + token,
                user.getId().toString(),
                RESET_TOKEN_TTL
        );

        String resetLink = baseUrl + "/auth/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        return token;
    }

    @Override
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        if (token == null || token.isBlank()) {
            throw new BadRequestException("Token is required");
        }
        if (newPassword == null || newPassword.isBlank() || confirmPassword == null || confirmPassword.isBlank()) {
            throw new BadRequestException("New password and confirm password are required");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("Confirm password does not match");
        }

        String key = RESET_TOKEN_KEY_PREFIX + token;
        String userIdValue = stringRedisTemplate.opsForValue().get(key);
        if (userIdValue == null) {
            throw new BadRequestException("Invalid or expired reset token");
        }

        Long userId;
        try {
            userId = Long.parseLong(userIdValue);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("Invalid reset token");
        }

        UserAccount user = getActiveUserById(userId);
        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new BadRequestException("New password must be different from current password");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userAccountRepository.save(user);
        stringRedisTemplate.delete(key);
    }

    @Override
    public UserAccount getActiveUserById(Long userId) {
        return userAccountRepository.findByIdAndStatus(userId, AccountStatus.active)
                .orElseThrow(() -> new BadRequestException("User not found or not active"));
    }

    @Override
    public void editProfile(Long userId, String fullName, String phoneNumber, String address) {
        UserAccount user = getActiveUserById(userId);
        if (fullName == null || fullName.isBlank()) {
            throw new BadRequestException("Full name is required");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new BadRequestException("Phone number is required");
        }

        user.setFullname(fullName.trim());
        user.setPhoneNumber(phoneNumber.trim());
        user.setAddress(address == null ? null : address.trim());
        userAccountRepository.save(user);
    }

    @Override
    public void deleteAccount(Long userId, String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is required");
        }

        UserAccount user = getActiveUserById(userId);
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BadRequestException("Incorrect password");
        }

        user.setStatus(AccountStatus.deleted);
        userAccountRepository.save(user);
    }
}

