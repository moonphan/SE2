package group12.ecwms.moonpham.common.dto.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterForm {
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
    private String address;
}

