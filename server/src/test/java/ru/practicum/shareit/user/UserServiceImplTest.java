package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers_whenInvoked_thenReturnedCollectionUsers() {
        User expectedUser = new User();
        when(userRepository.findAll()).thenReturn(List.of(expectedUser));

        List<UserDto> actualUsers = userService.getAll();

        assertEquals(actualUsers.size(), 1);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_whenUserFound_thenReturnedUser() {
        long userId = 0L;
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        UserDto actualUser = userService.getById(userId);

        assertEquals(expectedUser.getId(), actualUser.getId());
    }

    @Test
    void createUser_whenUInvoked_thenReturnCreatedUser() {
        long userId = 0L;
        User newUser = new User(userId, "User 1", "email@email.ru");
        UserDto newUserDto = UserMapper.toUserDto(newUser);
        when(userRepository.save(any())).thenReturn(newUser);

        UserDto savedUser = userService.create(newUserDto);

        assertEquals(newUser.getName(), savedUser.getName());
        assertEquals(newUser.getEmail(), savedUser.getEmail());
    }

    @Test
    void getUserById_whenUserNotFound_thenUserUserNotFoundExceptionThrown() {
        long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getById(userId));
        assertEquals(exception.getMessage(), "Не найден пользователь с id: 0");
    }

    @Test
    void userUpdate_whenUserFound_thenUpdatedAvailableFields() {
        long userId = 0L;
        User oldUser = new User();
        oldUser.setName("User 1");
        oldUser.setEmail("email@email.ru");

        UserDto newUser = new UserDto();
        newUser.setName("User 2");
        newUser.setEmail("email2@email.ru");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        UserDto savedUser = userService.update(newUser, userId);

        assertEquals("User 2", savedUser.getName());
        assertEquals("email2@email.ru", savedUser.getEmail());
    }

    @Test
    void userUpdate_whenUserFound_thenUpdatedOnlyEmailField() {
        long userId = 0L;
        User oldUser = new User();
        oldUser.setName("User 1");
        oldUser.setEmail("email@email.ru");

        UserDto newUser = new UserDto();
        newUser.setName(null);
        newUser.setEmail("email2@email.ru");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        UserDto savedUser = userService.update(newUser, userId);

        assertEquals("User 1", savedUser.getName());
        assertEquals("email2@email.ru", savedUser.getEmail());
    }

    @Test
    void userUpdate_whenUserFound_thenUpdatedOnlyNameField() {
        long userId = 0L;
        User oldUser = new User();
        oldUser.setName("User 1");
        oldUser.setEmail("email@email.ru");

        UserDto newUser = new UserDto();
        newUser.setName("User 2");
        newUser.setEmail(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        UserDto savedUser = userService.update(newUser, userId);

        assertEquals("User 2", savedUser.getName());
        assertEquals("email@email.ru", savedUser.getEmail());
    }

    @Test
    void userUpdate_whenUserFound_thenUpdateNothing() {
        long userId = 0L;
        User oldUser = new User();
        oldUser.setName("User 1");
        oldUser.setEmail("email@email.ru");

        UserDto newUser = new UserDto();
        newUser.setName("");
        newUser.setEmail("");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        UserDto savedUser = userService.update(newUser, userId);

        assertEquals("User 1", savedUser.getName());
        assertEquals("email@email.ru", savedUser.getEmail());
    }

    @Test
    void updateUser_whenUserNotFound_thenReturnUserNotFoundException() {
        long userId = 0L;

        assertThrows(UserNotFoundException.class, () -> userService.update(new UserDto(), userId));
    }

    @Test
    void deleteUserById_whenInvoked_thenReturned() {
        long userId = 0L;

        userService.deleteById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}