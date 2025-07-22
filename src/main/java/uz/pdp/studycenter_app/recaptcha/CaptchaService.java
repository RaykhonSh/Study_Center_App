package uz.pdp.studycenter_app.recaptcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class CaptchaService {
    @Value("${recaptcha.v2.secret}")
    private String recaptchaSecret;

    @Value("${recaptcha.verify.url}")
    private String recaptchaVerifyUrl;

//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }
//
//    @Autowired
//    private RestTemplate restTemplate;

    public boolean verifyCaptcha(String response){

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaSecret);
        params.add("response", response);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params, headers);
        GoogleResponse googleResponse=restTemplate.postForObject(recaptchaVerifyUrl,request,GoogleResponse.class);
        return googleResponse!=null && googleResponse.isSuccess();
    }
}
