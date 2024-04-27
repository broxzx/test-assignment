package ua.project.testassignment.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.project.testassignment.dto.RequestUserDto;
import ua.project.testassignment.dto.ResponseUserDto;
import ua.project.testassignment.entity.UserEntity;
import ua.project.testassignment.exception.DateRangeException;
import ua.project.testassignment.exception.UserNotFoundException;
import ua.project.testassignment.repository.UserRepository;
import ua.project.testassignment.utils.UserMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void getAllUsersResponseTest_whenUsersExist_thenReturnUsers() {
        UserEntity userEntity = new UserEntity();
        ResponseUserDto responseUserDto = new ResponseUserDto();

        when(userMapper.makeUserResponseDto(userEntity)).thenReturn(responseUserDto);
        when(userRepository.findAll()).thenReturn(Stream.of(userEntity).collect(Collectors.toList()));

        List<ResponseUserDto> allResponses = userService.getAllUsersResponse();

        Assertions.assertFalse(allResponses.isEmpty());
        assertEquals(responseUserDto, allResponses.get(0));
    }

    @Test
    public void getAllUsersResponseTest_whenNoUsersExist_thenReturnEmptyUsersList() {
        List<ResponseUserDto> allResponses = userService.getAllUsersResponse();

        assertTrue(allResponses.isEmpty());
    }

    @Test
    public void getUserEntityBetweenRangeTest_whenBetweenRangeIsValid_thenReturnResponseUsersList() {
        LocalDate from = LocalDate.of(2021, 1, 1);
        LocalDate to = LocalDate.of(2021, 12, 31);
        UserEntity userEntity = new UserEntity();
        ResponseUserDto responseUserDto = new ResponseUserDto();

        when(userMapper.makeUserResponseDto(userEntity)).thenReturn(responseUserDto);
        when(userRepository.findByBirthDateBetween(from, to)).thenReturn(Collections.singletonList(userEntity));

        List<ResponseUserDto> responses = userService.getUserEntityBetweenRange(from, to);

        Assertions.assertFalse(responses.isEmpty());
        assertEquals(responseUserDto, responses.get(0));
    }

    @Test
    public void getUserEntityBetweenRangeTest_whenBetweenRangeIsInvalid_thenReturnException() {
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2021, 12, 31);

        Exception exception = assertThrows(DateRangeException.class,
                () -> userService.getUserEntityBetweenRange(from, to));

        assertEquals("Invalid range. 'From' date should be before 'To' date.", exception.getMessage());
    }

    @Test
    public void getUserResponseByIdTest_whenUserExist_thenReturnUser() {
        String userId = "testId";
        UserEntity userEntity = new UserEntity();
        ResponseUserDto responseUserDto = new ResponseUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.makeUserResponseDto(userEntity)).thenReturn(responseUserDto);

        ResponseUserDto result = userService.getUserResponseById(userId);

        assertEquals(responseUserDto, result);
    }

    @Test
    public void getUserResponseByIdTest_whenUserIdNotFound_thenReturnException() {
        String userId = "testId";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getUserResponseById(userId);
        });

        assertEquals("user with id '" + userId + "' was not found", exception.getMessage());
    }

    @Test
    public void createUserEntityTest_whenValidRequestUserDto_thenReturnCreatedUser() {
        RequestUserDto requestUserDto = new RequestUserDto();
        UserEntity userEntity = new UserEntity();
        ResponseUserDto responseUserDto = new ResponseUserDto();

        when(userMapper.makeUserResponseDto(userEntity)).thenReturn(responseUserDto);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        ResponseUserDto result = userService.createUserEntity(requestUserDto);

        assertEquals(responseUserDto, result);
    }

    @Test
    public void deleteUserEntityTest_whenUserExist_thenReturnDeletedUserId() {
        String userId = "testId";

        String result = userService.deleteUserEntity(userId);

        assertEquals("user with id '%s' was deleted".formatted(userId), result);
    }

    @Test
    public void updateUserEntityTest_whenValidRequestUserDto_thenReturnUpdatedUser() {
        String userId = "testId";
        RequestUserDto requestUserDto = new RequestUserDto();
        UserEntity userEntity = new UserEntity();
        ResponseUserDto responseUserDto = new ResponseUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.makeUserResponseDto(userEntity)).thenReturn(responseUserDto);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        ResponseUserDto result = userService.updateUserEntity(userId, requestUserDto);

        assertEquals(responseUserDto, result);
    }


}

