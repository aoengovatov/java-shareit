package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsers_whenInvoked_thenResponseStatusOkAndReturnedCollectionUsersInBody() {
        List<UserDto> expectedUsers = List.of(new UserDto());
        when(userService.getAll()).thenReturn(expectedUsers);

        ResponseEntity<List<UserDto>> response = userController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUsers, response.getBody());
    }

    @Test
    void getUserById_whenInvoked_thenResponseStatusOkAndReturnedUserInBody() {
        long userId = 0L;
        UserDto expectedUser = new UserDto(userId, "User 1", "email@email.ru");
        when(userService.getById(userId)).thenReturn(expectedUser);

        ResponseEntity<UserDto> response = userController.getById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
    }

    @Test
    void createUser_whenInvoked_thenResponseStatusOkAndReturnedUserInBody() {
        UserDto expectedUser = new UserDto(0L, "User 1", "email@email.ru");
        when(userService.create(expectedUser)).thenReturn(expectedUser);

        ResponseEntity<UserDto> response = userController.create(expectedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
        verify(userService, times(1)).create(expectedUser);
    }

    @Test
    void updateUser_whenInvoked_thenResponseStatusOk() {
        long userId = 0L;
        UserDto expectedUser = new UserDto(userId, "User 1", "email@email.ru");
        when(userService.update(expectedUser, userId)).thenReturn(expectedUser);

        ResponseEntity<UserDto> response = userController.update(userId, expectedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).update(expectedUser, userId);
    }

    @Test
    void deleteUserById_whenInvoked_thenResponseStatusOk() {
        long userId = 0L;

        userController.deleteById(userId);

        verify(userService, times(1)).deleteById(userId);
    }
}