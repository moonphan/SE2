package group12.ecwms.moonpham.domain.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class WarrantyStatusConverter implements AttributeConverter<WarrantyStatus, String> {
    @Override
    public String convertToDatabaseColumn(WarrantyStatus attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case active -> "active";
            case expired -> "expired";
            case VOID -> "void";
        };
    }

    @Override
    public WarrantyStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData) {
            case "active" -> WarrantyStatus.active;
            case "expired" -> WarrantyStatus.expired;
            case "void" -> WarrantyStatus.VOID;
            default -> throw new IllegalArgumentException("Unknown WarrantyStatus: " + dbData);
        };
    }
}

