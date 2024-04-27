package ua.project.testassignment;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ua.project.testassignment.entity.UserEntity;
import ua.project.testassignment.repository.UserRepository;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication
@RequiredArgsConstructor
public class TestAssignmentApplication {

    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(TestAssignmentApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner() {
//        return args -> {
//            userRepository.save(UserEntity.builder()
//                            .id(String.valueOf(UUID.randomUUID()))
//                            .email("asd")
//                            .firstName("asd")
//                            .lastName("asd")
//                            .birthDate(LocalDate.now())
//                    .build());
//
//            System.out.println(userRepository.findAll());
//        };
//    }
}
