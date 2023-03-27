package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserClient userClient;

    @SneakyThrows
    @Test
    void getUserById() {
        long userId = 0L;

        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userClient, times(1)).getById(userId);
    }

    @SneakyThrows
    @Test
    void createUser_whenUserNameIsNull_returnStatusBadRequest() {
        UserDto userDto = new UserDto(0L, null, "mail@mail.ru");
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(userClient.create(userDto)).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(userClient, times(0)).create(userDto);
    }

    @SneakyThrows
    @Test
    void createUser_whenUserEmailIsNull_returnStatusBadRequest() {
        UserDto userToCreate = new UserDto(0L, "User 1", null);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(userClient.create(userToCreate)).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(userClient, times(0)).create(userToCreate);
    }

    @SneakyThrows
    @Test
    void createUser_whenUserEmailIsNotValid_returnStatusBadRequest() {
        UserDto userToCreate = new UserDto(0L, "User 1", "emailru");
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(userClient.create(userToCreate)).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(userClient, times(0)).create(userToCreate);
    }

    @SneakyThrows
    @Test
    void updateUser_whenUserEmailIsNotValid_returnStatusBadRequest() {
        Long userId = 0L;
        UserDto userToUpdate = new UserDto(userId, "User 1", "email.ru");

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(userClient, never()).update(userToUpdate, userId);
    }
}