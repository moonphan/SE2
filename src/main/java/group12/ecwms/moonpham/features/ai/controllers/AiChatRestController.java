package group12.ecwms.moonpham.features.ai.controllers;

import group12.ecwms.moonpham.features.ai.dto.AiChatRequest;
import group12.ecwms.moonpham.features.ai.dto.AiChatResponse;
import group12.ecwms.moonpham.features.ai.dto.AiStatusResponse;
import group12.ecwms.moonpham.features.ai.services.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiChatRestController {

    private final AiChatService aiChatService;

    @GetMapping("/status")
    public AiStatusResponse status() {
        return new AiStatusResponse(aiChatService.isServiceOnline());
    }

    @PostMapping("/chat")
    public ResponseEntity<AiChatResponse> chat(@RequestBody AiChatRequest request) {
        if (!aiChatService.isServiceOnline()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(AiChatResponse.fail("AI service is offline. Set GOOGLE_API_KEY in the environment."));
        }
        try {
            String msg = request != null ? request.message() : null;
            String reply = aiChatService.chat(msg);
            return ResponseEntity.ok(AiChatResponse.ok(reply));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(AiChatResponse.fail(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(AiChatResponse.fail("AI API is temporarily unavailable. Please try again later."));
        }
    }
}
