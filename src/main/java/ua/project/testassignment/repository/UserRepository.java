package ua.project.testassignment.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ua.project.testassignment.entity.UserEntity;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    List<UserEntity> findByBirthDateBetween(LocalDate from, LocalDate to);
}
