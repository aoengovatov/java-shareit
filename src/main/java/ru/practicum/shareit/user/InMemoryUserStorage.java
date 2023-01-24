package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.*;

@Service
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    private final Set<String> emailUniqSet = new HashSet<>();
    private long userId = 0;

    @Override
    public User create(User user) {
        if (checkEmail(user)) {
            user.setId(generateId());
            users.put(user.getId(), user);
            emailUniqSet.add(user.getEmail());
        }
        return user;
    }

    @Override
    public User update(User userUpdate) {
        if (users.containsKey(userUpdate.getId())) {
            User user = users.get(userUpdate.getId());
            if (!Objects.equals(userUpdate.getName(), user.getName())) {
                if (isName(userUpdate)) {
                    user.setName(userUpdate.getName());
                }
            }
            if (!Objects.equals(userUpdate.getEmail(), user.getEmail())) {
                if (checkEmail(userUpdate)) {
                    emailUniqSet.remove(user.getEmail());
                    emailUniqSet.add(userUpdate.getEmail());
                    user.setEmail(userUpdate.getEmail());
                }
            }
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
            emailUniqSet.remove(getById(userId).getEmail());
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

    private boolean checkEmail(User user) {
        if (user.getEmail() != null) {
            if (user.getEmail().isBlank()) {
                log.info("email имеет пустое значение: {}", user.getEmail());
                return false;
            }
            if (emailUniqSet.contains(user.getEmail())) {
                log.info("Пользователь с email {} уже зарегистрирован в системе", user.getEmail());
                throw new ValidationException("email double");
            }
        } else {
            return false;
        }
        return true;
    }
}