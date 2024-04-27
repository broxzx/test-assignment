package ua.project.testassignment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.project.testassignment.validation.Age;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestUserDto {

    @NotBlank(message = "First name cannot be blank or null")
    private String firstName;

    @NotBlank(message = "Second name cannot be blank or null")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank or null")
    private String email;

    @NotNull(message = "Birth date cannot be null")
    @Age
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}
