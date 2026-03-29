package group12.ecwms.moonpham.features.ai.dto;

public record AiChatResponse(String reply, String error) {
    public static AiChatResponse ok(String reply) {
        return new AiChatResponse(reply, null);
    }

    public static AiChatResponse fail(String error) {
        return new AiChatResponse(null, error);
    }
}
