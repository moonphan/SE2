package group12.ecwms.moonpham.features.auth.services;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String resetLink);
}

