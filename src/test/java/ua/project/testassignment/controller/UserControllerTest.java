package ua.project.testassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.project.testassignment.dto.RequestUserDto;
import ua.project.testassignment.dto.ResponseUserDto;
import ua.project.testassignment.exception.UserNotFoundException;
import ua.project.testassignment.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static final String END_POINT = "/users";

    private RequestUserDto requestUserDto;

    private ResponseUserDto responseUserDto;

    @BeforeEach
    void setUp() {
        requestUserDto = RequestUserDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address("Street 1 house 3rd")
                .birthDate(LocalDate.of(2000, 1, 1))
                .phoneNumber("038123951")
                .build();


        responseUserDto = ResponseUserDto.builder()
                .id("662cb4400726706c63bfbc4b")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address("Street 1 house 3rd")
                .birthDate(LocalDate.of(2000, 1, 1))
                .phoneNumber("038123951")
                .build();
    }


    @Test
    public void getAllUsers_whenUsersExist_thenReturnListResponseUsers() throws Exception {
        List<ResponseUserDto> responseUserDtos = List.of(responseUserDto, responseUserDto, responseUserDto);

        given(userService.getAllUsersResponse()).willReturn(responseUserDtos);

        this.mockMvc.perform(get(END_POINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(responseUserDtos)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getAllUsers_whenServiceThrowsException_thenInternalServerError() throws Exception {
        given(userService.getAllUsersResponse()).willThrow(new RuntimeException());

        this.mockMvc.perform(get(END_POINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();
    }


    @Test
    public void getUserById_whenExist_thenReturnUser() throws Exception {
        String userId = "662cb4400726706c63bfbc4b";

        given(userService.getUserResponseById(userId)).willReturn(responseUserDto);

        this.mockMvc.perform(get(END_POINT + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getUserById_whenNotExist_thenNotFound() throws Exception {
        String nonExistingId = "nonexistent-user-id";

        given(userService.getUserResponseById(nonExistingId)).willThrow(UserNotFoundException.class);

        this.mockMvc.perform(get(END_POINT + "/" + nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void createUserEntity_whenValidRequest_thenReturnCreatedUser() throws Exception {
        given(userService.createUserEntity(requestUserDto)).willReturn(responseUserDto);

        this.mockMvc.perform(post(END_POINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void createUserEntity_whenInvalidRequest_thenReturnException() throws Exception {
        requestUserDto.setBirthDate(LocalDate.now());
        requestUserDto.setFirstName("");
        requestUserDto.setLastName(null);
        String userDtoJson = objectMapper.writeValueAsString(requestUserDto);

        this.mockMvc.perform(post(END_POINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(Matchers.containsInAnyOrder("lastName", "firstName", "birthDate")));
    }

    @Test
    public void updateUserEntity_whenValidRequest_thenReturnUpdatedUser() throws Exception {
        String userId = "662cb4400726706c63bfbc4b";
        given(userService.updateUserEntity(eq(userId), any(RequestUserDto.class))).willReturn(responseUserDto);

        this.mockMvc.perform(put(END_POINT + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void updateUserEntity_whenInvalidRequest_thenReturnException() throws Exception {
        String userId = "662cb4400726706c63bfbc4b";
        requestUserDto.setBirthDate(LocalDate.now());
        requestUserDto.setFirstName("");
        requestUserDto.setLastName(null);
        String userDtoJson = objectMapper.writeValueAsString(requestUserDto);

        this.mockMvc.perform(put(END_POINT + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(Matchers.containsInAnyOrder("lastName", "firstName", "birthDate")));
    }

    @Test
    public void deleteUserEntity_whenValidRequest_thenReturnDeletedUserId() throws Exception {
        String userId = "662cb4400726706c63bfbc4b";
        given(userService.deleteUserEntity(userId)).willReturn(userId);

        this.mockMvc.perform(delete(END_POINT + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        content().string(userId))
                .andReturn();
    }

    @Test
    public void deleteUserEntity_whenInvalidRequest_thenReturnException() throws Exception {
        String userId = "nonexistent-user-id";
        given(userService.deleteUserEntity(userId)).willThrow(UserNotFoundException.class);

        this.mockMvc.perform(delete(END_POINT + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
