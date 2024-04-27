package ua.project.testassignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.project.testassignment.dto.RequestUserDto;
import ua.project.testassignment.dto.ResponseUserDto;
import ua.project.testassignment.entity.UserEntity;
import ua.project.testassignment.exception.DateRangeException;
import ua.project.testassignment.exception.UserNotFoundException;
import ua.project.testassignment.repository.UserRepository;
import ua.project.testassignment.utils.UserMapper;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<ResponseUserDto> getAllUsersResponse() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::makeUserResponseDto)
                .toList();
    }

    public ResponseUserDto getUserResponseById(String userId) {
        UserEntity userEntity = getUserEntityById(userId);

        return userMapper
                .makeUserResponseDto(userEntity);
    }

    public ResponseUserDto createUserEntity(RequestUserDto requestUserDto) {
        UserEntity userEntity = makeUserEntityFromRequestUser(requestUserDto);

        userRepository.save(userEntity);

        return userMapper
                .makeUserResponseDto(userEntity);
    }

    public ResponseUserDto updateUserEntity(String userId, RequestUserDto requestUserDto) {
        UserEntity userEntity = getUserEntityById(userId);

        UserEntity updatedUserEntity = makeUserEntityFromRequestUser(requestUserDto);
        updatedUserEntity.setId(userEntity.getId());

        userRepository.save(updatedUserEntity);
        return userMapper.makeUserResponseDto(updatedUserEntity);
    }

    public String deleteUserEntity(String userId) {
        getUserEntityById(userId);
        userRepository.deleteById(userId);

        return "user with id '%s' was deleted".formatted(userId);
    }

    public List<ResponseUserDto> getUserEntityBetweenRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new DateRangeException("Invalid range. 'From' date should be before 'To' date.");
        }

        List<UserEntity> byBirthDateBetween = userRepository.findByBirthDateBetween(from, to);

        return byBirthDateBetween.stream()
                .map(userMapper::makeUserResponseDto)
                .toList();
    }

    private UserEntity getUserEntityById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException("user with id '%s' was not found".formatted(userId))
                );
    }

    private UserEntity makeUserEntityFromRequestUser(RequestUserDto requestUserDto) {
        return UserEntity.builder()
                .firstName(requestUserDto.getFirstName())
                .lastName(requestUserDto.getLastName())
                .email(requestUserDto.getEmail())
                .birthDate(requestUserDto.getBirthDate())
                .address(requestUserDto.getAddress())
                .phoneNumber(requestUserDto.getPhoneNumber())
                .build();
    }
}
