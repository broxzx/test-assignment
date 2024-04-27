package ua.project.testassignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.project.testassignment.dto.RequestUserDto;
import ua.project.testassignment.dto.ResponseUserDto;
import ua.project.testassignment.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private static final String GET_USER_BY_ID = "/{id}";
    private static final String UPDATE_USER_BY_ID = "/{id}";
    private static final String DELETE_USER_BY_ID = "/{id}";
    private static final String GET_BY_BIRTH_DATE = "/byBirthDateRange";

    @GetMapping
    public ResponseEntity<List<ResponseUserDto>> getAllUsers() {
        return ResponseEntity
                .ok(userService.getAllUsersResponse());
    }

    @GetMapping(GET_USER_BY_ID)
    public ResponseEntity<ResponseUserDto> getUserById(@PathVariable("id") String id) {
        return ResponseEntity
                .ok(userService.getUserResponseById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseUserDto> createUser(@RequestBody @Valid RequestUserDto requestUserDto) {
        ResponseUserDto responseUserDto = userService.createUserEntity(requestUserDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseUserDto);
    }

    @PutMapping(UPDATE_USER_BY_ID)
    public ResponseEntity<ResponseUserDto> updateUser(@PathVariable String id, @RequestBody @Valid RequestUserDto requestUserDto) {
        ResponseUserDto responseUserDto = userService.updateUserEntity(id, requestUserDto);

        return ResponseEntity
                .ok(responseUserDto);
    }

    @DeleteMapping(DELETE_USER_BY_ID)
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return ResponseEntity
                .ok(userService.deleteUserEntity(id));
    }

    @GetMapping(GET_BY_BIRTH_DATE)
    public ResponseEntity<List<ResponseUserDto>> getUserByBirthDate(@RequestParam(name = "from") LocalDate from,
                                                                    @RequestParam(name = "to") LocalDate to) {
        List<ResponseUserDto> userEntityBetweenRange = userService.getUserEntityBetweenRange(from, to);

        return ResponseEntity
                .ok(userEntityBetweenRange);
    }
}
