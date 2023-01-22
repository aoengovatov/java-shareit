package ru.practicum.shareit.user;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserStorage {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    List<User> getAll();

    Optional<UserDto> getById(Long userId);

    void deleteById(Long userId);
}
