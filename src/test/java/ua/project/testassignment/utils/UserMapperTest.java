package ua.project.testassignment.utils;

import org.junit.jupiter.api.Test;
import ua.project.testassignment.dto.ResponseUserDto;
import ua.project.testassignment.entity.UserEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    @Test
    public void testMakeUserResponseDto() {
        UserMapper userMapper = new UserMapper();
        UserEntity userEntity = UserEntity.builder()
                .id("TestID")
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .email("test@test.com")
                .birthDate(LocalDate.of(1987, 8, 4))
                .address("Test Address")
                .phoneNumber("1234567890")
                .build();

        ResponseUserDto responseUserDto = userMapper.makeUserResponseDto(userEntity);

        assertEquals("TestID", responseUserDto.getId());
        assertEquals("TestFirstName", responseUserDto.getFirstName());
        assertEquals("TestLastName", responseUserDto.getLastName());
        assertEquals("test@test.com", responseUserDto.getEmail());
        assertEquals("1987-08-04", responseUserDto.getBirthDate().toString());
        assertEquals("Test Address", responseUserDto.getAddress());
        assertEquals("1234567890", responseUserDto.getPhoneNumber());
    }
}