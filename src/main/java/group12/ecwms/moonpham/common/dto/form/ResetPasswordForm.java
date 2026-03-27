package group12.ecwms.moonpham.common.dto.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordForm {
    private String token;
    private String newPassword;
    private String confirmPassword;
}

