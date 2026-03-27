package group12.ecwms.moonpham.common.dto.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckoutForm {
    private String shippingAddress;
    private String shippingMethod;
    private String paymentMethod;
}

