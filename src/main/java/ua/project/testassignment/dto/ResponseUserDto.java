package ua.project.testassignment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ResponseUserDto {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}
