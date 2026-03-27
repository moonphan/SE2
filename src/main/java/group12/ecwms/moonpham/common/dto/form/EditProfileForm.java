package group12.ecwms.moonpham.common.dto.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditProfileForm {
    private String fullName;
    private String phoneNumber;
    private String address;
}

