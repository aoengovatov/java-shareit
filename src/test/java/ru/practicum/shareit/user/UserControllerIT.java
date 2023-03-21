package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    private UserService userService;

    @SneakyThrows
    @Test
    void getAll() {

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1)).getAll();
    }

    @SneakyThrows
    @Test
    void getUserById() {
        long userId = 0L;

        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1)).getById(userId);
    }

    @SneakyThrows
    @Test
    void createUser_whenUserNameIsNull_returnStatusBadRequest() {
        UserDto userToCreate = new UserDto(0L, null, "mail@mail.ru");
        when(userService.create(userToCreate)).thenReturn(userToCreate);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(userService, times(0)).create(userToCreate);
    }

    @SneakyThrows
    @Test
    void createUser_whenUserEmailIsNull_returnStatusBadRequest() {
        UserDto userToCreate = new UserDto(0L, "User 1", null);
        when(userService.create(userToCreate)).thenReturn(userToCreate);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(userService, times(0)).create(userToCreate);
    }

    @SneakyThrows
    @Test
    void createUser_whenUserEmailIsNotValid_returnStatusBadRequest() {
        UserDto userToCreate = new UserDto(0L, "User 1", "emailru");
        when(userService.create(userToCreate)).thenReturn(userToCreate);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(userService, times(0)).create(userToCreate);
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

        verify(userService, never()).update(userToUpdate, userId);
    }

    @SneakyThrows
    @Test
    void deleteUser_whenInvoked_returnStatusOk() {
        Long userId = 0L;

        mockMvc.perform(delete("/users/{userId}", userId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userService, times(1)).deleteById(userId);
    }
}