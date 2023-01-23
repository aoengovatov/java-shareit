package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.*;

@Service
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long userId = 0;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setId(generateId());
        users.put(user.getId(), user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        if (users.containsKey(userDto.getId())) {
            User user = users.get(userDto.getId());
            if (isName(userDto)) {
                user.setName(userDto.getName());
            }
            if (isEmail(userDto)) {
                user.setEmail(userDto.getEmail());
            }
            users.put(userDto.getId(), user);
            log.info("Обновление пользователя с id: {}", userDto.getId());
            return UserMapper.toUserDto(user);
        } else {
            log.info("Не найден пользователь с id: {}", userDto.getId());
            throw new UserNotFoundException("id");
        }
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public UserDto getById(Long userId) {
        if (users.containsKey(userId)) {
            log.info("Найден пользователь с id: {}", userId);
            return UserMapper.toUserDto(users.get(userId));
        } else {
            log.info("Не найден пользователь с id: {}", userId);
            throw new UserNotFoundException("id");
        }
    }

    @Override
    public void deleteById(Long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        } else {
            log.info("Не найден пользователь с id: {}", userId);
            throw new UserNotFoundException("id");
        }
    }

    private long generateId() {
        return ++userId;
    }

    private boolean isName(UserDto userDto) {
        if (userDto.getName() != null) {
            if (!userDto.getName().isBlank()) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmail(UserDto userDto) {
        if (userDto.getEmail() != null) {
            if (!userDto.getEmail().isBlank()) {
                return true;
            }
        }
        return false;
    }
}