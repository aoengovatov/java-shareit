package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectParameterException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserStorage userStorage;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userStorage.create(UserMapper.toUser(userDto));
        log.info("Добавлен новый user с id: {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) {
        checkNegativeUserId(userId);
        userDto.setId(userId);
        return UserMapper.toUserDto(userStorage.update(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto getById(Long userId) {
        return UserMapper.toUserDto(userStorage.getById(userId));
    }

    @Override
    public void deleteById(Long userId) {
        userStorage.deleteById(userId);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    private void checkNegativeUserId(Long userId) {
        if (userId <= 0) {
            log.info("Запрос пользователя с неверным id: {}", userId);
            throw new IncorrectParameterException("id");
        }
    }
}