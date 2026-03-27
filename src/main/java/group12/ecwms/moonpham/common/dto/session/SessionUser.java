package group12.ecwms.moonpham.common.dto.session;

import group12.ecwms.moonpham.domain.enums.UserRole;

public record SessionUser(
        Long id,
        String username,
        String email,
        UserRole role
) {
}

