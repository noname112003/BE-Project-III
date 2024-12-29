package sapo.com.validator.phone;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TenCharacterValidator.class)
public @interface TenCharacter {
    String message() default "Phone Number must 10 number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
