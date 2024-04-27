package ua.project.testassignment.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {

    @Value("${user.age}")
    private Long age;

    @Override
    public void initialize(Age age) {
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        return birthDate != null && Period.between(birthDate, LocalDate.now()).getYears() >= age;
    }
}
