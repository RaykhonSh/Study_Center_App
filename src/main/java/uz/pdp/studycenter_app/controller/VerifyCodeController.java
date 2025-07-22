package uz.pdp.studycenter_app.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.studycenter_app.dto.UserDto;
import uz.pdp.studycenter_app.entity.Users;
import uz.pdp.studycenter_app.repo.UsersRepository;
import uz.pdp.studycenter_app.service.RoleService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/verification")
public class VerifyCodeController {

    private final JavaMailSenderImpl mailSender;
    private static final int MAX_ATTEMPTS = 3;
    private static final int EXPIRE_SECONDS = 60;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    public VerifyCodeController(JavaMailSenderImpl mailSender, RoleService roleService, PasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.mailSender = mailSender;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }

    @GetMapping
    public String page(Model model, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/register";
        }
        // 1. Yangi kod, expiry va urinishlarga sozlash
        String code = generateVerificationCode();
        LocalDateTime expiry = LocalDateTime.now().plusSeconds(EXPIRE_SECONDS);
        session.setAttribute("code", code);
        session.setAttribute("expiry", expiry);
        session.setAttribute("attemptsLeft", MAX_ATTEMPTS);

        sendVerificationCode(user.getEmail(), code);

        // 2. Frontend uchun qolgan vaqtni sekundda berish
        long remaining = Duration.between(LocalDateTime.now(), expiry).getSeconds();
        model.addAttribute("remainingSeconds", Math.max(remaining, 0));
        return "verification";
    }

    @PostMapping("/verify")
    @ResponseBody
    public Map<String, Object> verifyApi(@RequestBody Map<String, String> body, HttpSession session) {
        System.out.println("📩 /verification/verify API ishladi!");

        String inputCode = body.get("code");
        LocalDateTime expiry = (LocalDateTime) session.getAttribute("expiry");
        Integer attemptsLeft = (Integer) session.getAttribute("attemptsLeft");

        Map<String, Object> resp = new HashMap<>();
        long remaining = expiry == null ? 0
                : Math.max(Duration.between(LocalDateTime.now(), expiry).getSeconds(), 0);

        // 1) Muddati tugagan yoki urinishlar qolmagan
        if (remaining <= 0 || attemptsLeft == null || attemptsLeft <= 0) {
            resp.put("success", false);
            resp.put("message", "Your code has expired. Please resend.");
            resp.put("remainingSeconds", 0);
            resp.put("attemptsLeft", 0);
            return resp;
        }

        // 2) To‘g‘ri kiritsa
        String realCode = (String) session.getAttribute("code");
        if (realCode != null && realCode.equals(inputCode)) {
            // ro‘yxatdan o‘tkazish
            UserDto userDto = (UserDto) session.getAttribute("user");
            if (userDto == null) {
                resp.put("success", false);
                resp.put("message", "Session expired. Please register again.");
                return resp;
            }
            if (!userDto.getPassword().equals(userDto.getPasswordRepeat())) {
                resp.put("success", false);
                resp.put("message","Passwords do not match.");
                return resp;
            }

            Optional<Users> byEmail = usersRepository.findByEmail(userDto.getEmail());
            if (byEmail.isPresent()) {
                resp.put("success", false);
                resp.put("message", "Email already in use.");
                return resp;
            }
            registerUserDto(userDto);

            session.removeAttribute("user");
            resp.put("success", true);
            resp.put("message", "Verified successfully. Redirecting…");
            return resp;
        }

        // 3) Noto‘g‘ri kiritsa
        attemptsLeft--;
        session.setAttribute("attemptsLeft", attemptsLeft);
        if (attemptsLeft <= 0) {
            resp.put("success", false);
            resp.put("message", "Too many attempts. Your code has expired.");
            resp.put("remainingSeconds", 0);
            resp.put("attemptsLeft", 0);
        } else {
            resp.put("success", false);
            resp.put("message", "Incorrect code. You have " + attemptsLeft + " attempt(s) left.");
            resp.put("remainingSeconds", remaining);
            resp.put("attemptsLeft", attemptsLeft);
        }
        return resp;
    }

    @PostMapping("/resend")
    @ResponseBody
    public Map<String, Object> resendApi(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        Map<String, Object> resp = new HashMap<>();

        if (user == null) {
            resp.put("success", false);
            resp.put("message", "Session expired, please register again.");
            return resp;
        }

        // Yangi kod va expiry
        String code = generateVerificationCode();
        LocalDateTime expiry = LocalDateTime.now().plusSeconds(EXPIRE_SECONDS);
        session.setAttribute("code", code);
        session.setAttribute("expiry", expiry);
        session.setAttribute("attemptsLeft", MAX_ATTEMPTS);

        sendVerificationCode(user.getEmail(), code);

        long remaining = Duration.between(LocalDateTime.now(), expiry).getSeconds();
        resp.put("success", true);
        resp.put("message", "A new code has been sent to your email.");
        resp.put("remainingSeconds", Math.max(remaining, 0));
        resp.put("attemptsLeft", MAX_ATTEMPTS);
        return resp;
    }

    // --- yordamchi metodlar ---
    private void sendVerificationCode(String email, String verificationCode) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Verification Code");
        msg.setText("Your code is: " + verificationCode);
        mailSender.send(msg);
    }

    private String generateVerificationCode() {
        return String.format("%05d", (int) (Math.random() * 100000));
    }

    private void registerUserDto(UserDto userDto) {
        // --- ro‘yxatdan o‘tkazish jarayoni ---
        Users users = Users.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .roles(roleService.getSimpleRoles())
                .build();
        usersRepository.save(users);
    }
}
