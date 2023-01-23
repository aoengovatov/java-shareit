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
    public User create(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User userUpdate) {
        if (users.containsKey(userUpdate.getId())) {
            User user = users.get(userUpdate.getId());
            if (isName(userUpdate)) {
                user.setName(userUpdate.getName());
            }
            if (isEmail(userUpdate)) {
                user.setEmail(userUpdate.getEmail());
            }
            users.put(user.getId(), user);
            log.info("Обновление пользователя с id: {}", user.getId());
            return user;
        } else {
            log.info("Не найден пользователь с id: {}", userUpdate.getId());
            throw new UserNotFoundException("id");
        }
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(Long userId) {
        if (users.containsKey(userId)) {
            log.info("Найден пользователь с id: {}", userId);
            return users.get(userId);
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

    private boolean isName(User user) {
        if (user.getName() != null) {
            if (!user.getName().isBlank()) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmail(User user) {
        if (user.getEmail() != null) {
            if (!user.getEmail().isBlank()) {
                return true;
            }
        }
        return false;
    }
}