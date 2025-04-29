package uz.pdp.studycenter_app.anotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uz.pdp.studycenter_app.dto.UserDto;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserDto> {
    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {
        if (userDto.getPassword()==null||userDto.getPasswordRepeat()==null) {
            return false;
        }

        boolean matched=userDto.getPassword().equals(userDto.getPasswordRepeat());

        if (!matched) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("passwordRepeat")
                    .addConstraintViolation();
        }
        return matched;
    }
}
