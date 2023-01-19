package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);
    UserDto update(UserDto userDto, Long userId);
    UserDto getById(Long userId);
    void deleteById(Long userId);
    List<User> getAll();
}
