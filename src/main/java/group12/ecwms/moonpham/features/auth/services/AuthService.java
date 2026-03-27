package group12.ecwms.moonpham.features.auth.services;

import group12.ecwms.moonpham.common.dto.request.LoginRequest;
import group12.ecwms.moonpham.common.dto.request.RegisterRequest;
import group12.ecwms.moonpham.common.dto.response.LoginResponse;
import group12.ecwms.moonpham.common.dto.response.RegisterResponse;
import group12.ecwms.moonpham.domain.entity.UserAccount;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    String forgotPassword(String email);

    void resetPassword(String token, String newPassword, String confirmPassword);

    UserAccount getActiveUserById(Long userId);

    void editProfile(Long userId, String fullName, String phoneNumber, String address);

    void deleteAccount(Long userId, String password);
}

