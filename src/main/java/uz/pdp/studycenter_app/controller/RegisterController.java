package uz.pdp.studycenter_app.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.studycenter_app.dto.UserDto;
import uz.pdp.studycenter_app.service.CaptchaService;

import java.io.IOException;

@Controller
@RequestMapping("/register")
public class RegisterController {
    private final CaptchaService captchaService;

    public RegisterController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping
    public String register(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute("userDto") UserDto userDto,
                           BindingResult result,
                           Model model,
                           HttpSession session,
                           HttpServletResponse response,
                           @RequestParam(name = "g-recaptcha-response") String captchaResponse) throws IOException {
        if (result.hasErrors()) {
            return "register";
        }

        boolean isCaptchaValid=captchaService.verifyCaptcha(captchaResponse);
        if (!isCaptchaValid) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute("captchaError","Please enter a valid captcha");
            return "register";
        }

        session.setAttribute("user", userDto);
        return "redirect:/verification";
    }
}
