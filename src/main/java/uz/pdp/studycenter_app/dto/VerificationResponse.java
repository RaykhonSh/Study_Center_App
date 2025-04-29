package uz.pdp.studycenter_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerificationResponse {
    private String status;
    private String message;
    private long remainingTime;
    private int attemptsLeft;
}
