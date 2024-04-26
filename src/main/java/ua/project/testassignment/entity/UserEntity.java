package ua.project.testassignment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "users")
@AllArgsConstructor
@Data
@Builder
public class UserEntity {

    @Transient
    public static final String sequenceName = "user_sequence";

    @Id
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String address;

    private String phoneNumber;

}