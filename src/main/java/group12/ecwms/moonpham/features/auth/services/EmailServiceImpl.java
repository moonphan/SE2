package group12.ecwms.moonpham.features.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Reset your password");
        message.setText(
                "We received a password reset request for your account.\n\n"
                        + "Click the link below to reset your password:\n"
                        + resetLink + "\n\n"
                        + "This link will expire in 15 minutes.\n"
                        + "If you did not request this, please ignore this email."
        );
        mailSender.send(message);
    }
}

