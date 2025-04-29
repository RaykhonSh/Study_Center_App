package uz.pdp.studycenter_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import uz.pdp.studycenter_app.anotations.PasswordMatch;

@PasswordMatch
@Data
public class UserDto {
    @NotBlank(message = "First name must not be blank")
    @Size(min = 2, message = "First name must be at least 2 characters")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Size(min = 2, message = "Last name must be at least 2 characters")
    private String lastName;

    @NotBlank(message = "Password must not be blank")
    // Agar murakkablik talab qilinayotgan bo‘lsa, pattern yoki qo‘shimcha annotatsiyalar qo‘shishingiz mumkin.
    private String password;

    @NotBlank(message = "Repeat password must not be blank")
    private String passwordRepeat;

//    @NotBlank(message = "Phone number is required")
//    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only digits")
//    private String phone;

    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", message = "Please provide a valid email address")
    private String email;
}
