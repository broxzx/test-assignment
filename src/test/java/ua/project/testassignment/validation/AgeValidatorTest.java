package ua.project.testassignment.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDate;
import java.time.Period;

public class AgeValidatorTest {

    private final ConstraintValidatorContext context = null;

    @Test
    public void testIsValid() {

        AgeValidator ageValidator = new AgeValidator();
        ageValidator.setAge(18L);

        LocalDate birthDateValid = LocalDate.now().minusYears(20);
        LocalDate birthDateInvalid = LocalDate.now().minusYears(10);

        Assertions.assertTrue(ageValidator.isValid(birthDateValid, context));
        Assertions.assertFalse(ageValidator.isValid(birthDateInvalid, context));
    }

    @Test
    public void testIsValidWithNullBirthDate() {

        AgeValidator ageValidator = new AgeValidator();
        ageValidator.setAge(18L);

        Assertions.assertFalse(ageValidator.isValid(null, context));
    }
}