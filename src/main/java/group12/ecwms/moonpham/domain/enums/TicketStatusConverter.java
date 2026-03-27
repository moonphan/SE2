package group12.ecwms.moonpham.domain.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TicketStatusConverter implements AttributeConverter<TicketStatus, String> {
    @Override
    public String convertToDatabaseColumn(TicketStatus attribute) {
        if (attribute == null) return null;
        return switch (attribute) {
            case NEW -> "new";
            case IN_PROGRESS -> "in_progress";
            case RESOLVED -> "resolved";
        };
    }

    @Override
    public TicketStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData) {
            case "new" -> TicketStatus.NEW;
            case "in_progress" -> TicketStatus.IN_PROGRESS;
            case "resolved" -> TicketStatus.RESOLVED;
            default -> throw new IllegalArgumentException("Unknown TicketStatus: " + dbData);
        };
    }
}

