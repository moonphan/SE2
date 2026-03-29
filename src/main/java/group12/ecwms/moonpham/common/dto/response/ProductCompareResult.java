package group12.ecwms.moonpham.common.dto.response;

import group12.ecwms.moonpham.domain.entity.Product;

import java.math.BigDecimal;

public record ProductCompareResult(
        Product productA,
        Product productB,
        BigDecimal minPriceA,
        BigDecimal minPriceB,
        String errorMessage
) {
    public boolean ok() {
        return errorMessage == null && productA != null && productB != null;
    }
}
