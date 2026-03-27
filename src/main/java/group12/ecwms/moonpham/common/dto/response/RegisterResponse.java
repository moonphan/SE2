package group12.ecwms.moonpham.common.dto.response;

public record RegisterResponse(
        Long userId,
        String username,
        String email,
        String message
) {
}

