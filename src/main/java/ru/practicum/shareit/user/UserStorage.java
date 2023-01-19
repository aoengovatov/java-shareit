package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface UserStorage {
    public UserDto createUser(UserDto userDto);
    public UserDto updateUser(UserDto userDto);
    List<User> getAll();
    Optional<UserDto> getById(Long userId);
    void deleteById(Long userId);
}
