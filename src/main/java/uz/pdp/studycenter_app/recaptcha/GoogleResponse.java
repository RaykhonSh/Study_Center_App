package uz.pdp.studycenter_app.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleResponse {
    private boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    private String hostName;

    @JsonProperty("error-codes")
    private String errorCodes;
}
