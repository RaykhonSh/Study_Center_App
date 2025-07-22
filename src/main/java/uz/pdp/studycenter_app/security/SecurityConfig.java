package uz.pdp.studycenter_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable);
//        http.csrf(csrf->{
//            csrf.ignoringRequestMatchers("/verification/**");
//        });
        http.authorizeHttpRequests(auth->{
            auth
                    .requestMatchers("/login/**","/register/**","/verification/**").permitAll()
                    .requestMatchers("/css/**", "/js/**").permitAll()
                    .anyRequest().authenticated();
        });

        http.formLogin(f->{
            f.loginPage("/login")
                    .usernameParameter("email")
                    .defaultSuccessUrl("/cabinet");
        });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
