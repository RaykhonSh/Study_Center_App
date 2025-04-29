package uz.pdp.studycenter_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "First name must not be blank")
    @Size(min = 2, message = "First name must be at least 2 characters")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Last name must not be blank")
    @Size(min = 2, message = "Last name must be at least 2 characters")
    private String lastName;

    @Column(nullable = false)
    @NotBlank(message = "Password must not be blank")
    //@Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "Repeat password must not be blank")
    private String repeatPassword;

    @Column(nullable = false)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only digits")
    private String phone;

    @Column(nullable = false)
    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", message = "Please provide a valid email address")
    private String email;


}
