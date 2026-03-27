package group12.ecwms.moonpham.common.dto.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginForm {
    private String loginId;
    private String password;
}

