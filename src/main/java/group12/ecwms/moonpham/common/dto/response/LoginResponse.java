package group12.ecwms.moonpham.common.dto.response;

import group12.ecwms.moonpham.domain.enums.UserRole;

public record LoginResponse(
        Long userId,
        String username,
        String email,
        UserRole role
) {
}

