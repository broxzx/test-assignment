package ua.project.testassignment.utils;

import org.springframework.stereotype.Component;
import ua.project.testassignment.dto.ResponseUserDto;
import ua.project.testassignment.entity.UserEntity;

@Component
public class UserMapper {

    public ResponseUserDto makeUserResponseDto(UserEntity user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
