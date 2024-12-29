package sapo.com.validator.phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;

public class TenCharacterValidator implements ConstraintValidator<TenCharacter , String>{


    @Override
    public void initialize(TenCharacter constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && value.length() == 10;
    }
}
