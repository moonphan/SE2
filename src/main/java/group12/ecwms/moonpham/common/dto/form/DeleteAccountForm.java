package group12.ecwms.moonpham.common.dto.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteAccountForm {
    private String password;
    /** Must be true to match activity diagram (user confirms deletion). */
    private Boolean confirmDelete;
}

