package group12.ecwms.moonpham.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name must be at most 100 characters")
        String fullName,

        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
        @Pattern(
                regexp = "^[a-zA-Z0-9._-]+$",
                message = "Username can only contain letters, numbers, dot, underscore, and dash"
        )
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        @Size(max = 150, message = "Email must be at most 150 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "Password must include uppercase, lowercase, and a number"
        )
        String password,

        @NotBlank(message = "Confirm password is required")
        String confirmPassword,

        @NotBlank(message = "Phone number is required")
        @Size(max = 30, message = "Phone number must be at most 30 characters")
        String phoneNumber,

        @Size(max = 255, message = "Address must be at most 255 characters")
        String address
) {
}

