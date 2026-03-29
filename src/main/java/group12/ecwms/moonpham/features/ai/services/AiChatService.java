package group12.ecwms.moonpham.features.ai.services;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import group12.ecwms.moonpham.domain.entity.Product;
import group12.ecwms.moonpham.features.products.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Google Gen AI SDK (Gemini) — consult assistant flow from SRS/activity diagram.
 */
@Service
@Slf4j
public class AiChatService {

    private static final int PRODUCT_CATALOG_LIMIT = 60;

    private final Client client;
    private final String model;
    private final String baseUrl;
    private final ProductRepository productRepository;

    public AiChatService(
            @Value("${app.genai.api-key:}") String apiKey,
            @Value("${app.genai.model:gemini-2.0-flash}") String model,
            @Value("${app.base-url:http://localhost:8080}") String baseUrl,
            ProductRepository productRepository
    ) {
        this.model = model;
        this.baseUrl = baseUrl == null ? "" : baseUrl.replaceAll("/$", "");
        this.productRepository = productRepository;
        if (apiKey != null && !apiKey.isBlank()) {
            this.client = Client.builder().apiKey(apiKey.trim()).build();
        } else {
            this.client = null;
        }
    }

    /** Diagram: "AI service online?" — true when API key is configured. */
    public boolean isServiceOnline() {
        return client != null;
    }

    public String chat(String userMessage) {
        if (client == null) {
            throw new IllegalStateException("AI service is not configured");
        }
        if (userMessage == null || userMessage.isBlank()) {
            throw new IllegalArgumentException("Message is required");
        }
        if (userMessage.length() > 8000) {
            throw new IllegalArgumentException("Message is too long");
        }

        String catalog = buildProductCatalogLines();
        String systemText = """
                You are a helpful shopping assistant for an electronics e-commerce site (Moonpham).
                Answer briefly and clearly. When you recommend a product from our catalog, use a markdown link:
                [Product name](BASE_URL/product-detail/PRODUCT_ID)
                Use only product IDs listed below. If nothing matches, suggest browsing /products and do not invent IDs.
                Catalog (id | name | brand):
                CATALOG
                """
                .replace("BASE_URL", baseUrl)
                .replace("CATALOG", catalog);

        Content systemInstruction = Content.fromParts(Part.fromText(systemText));
        GenerateContentConfig config = GenerateContentConfig.builder()
                .systemInstruction(systemInstruction)
                .maxOutputTokens(2048)
                .build();

        Content userContent = Content.fromParts(Part.fromText(userMessage.trim()));

        try {
            GenerateContentResponse response = client.models.generateContent(model, userContent, config);
            String text = response.text();
            if (text == null || text.isBlank()) {
                return "(No text returned from the model.)";
            }
            return text.trim();
        } catch (Exception e) {
            log.warn("Gen AI generateContent failed: {}", e.toString());
            throw new RuntimeException("AI API error", e);
        }
    }

    private String buildProductCatalogLines() {
        var page = productRepository.findAll(PageRequest.of(0, PRODUCT_CATALOG_LIMIT));
        StringBuilder sb = new StringBuilder();
        for (Product p : page.getContent()) {
            sb.append(p.getId())
                    .append(" | ")
                    .append(p.getName() == null ? "" : p.getName())
                    .append(" | ")
                    .append(p.getBrand() == null ? "-" : p.getBrand())
                    .append('\n');
        }
        if (sb.isEmpty()) {
            return "(no products loaded)";
        }
        return sb.toString();
    }
}
