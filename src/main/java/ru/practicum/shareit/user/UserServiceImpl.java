package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectParameterException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    UserStorage userStorage;

    @Override
    public UserDto create(UserDto userDto) {
        checkEmailUnique(userDto.getEmail());
        UserDto user = userStorage.createUser(userDto);
        log.info("Добавлен новый user с id: " + user.getId());
        return user;
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) {
        checkNegativeUserId(userId);
        if(getById(userId) != null){
            userDto.setId(userId);
            if(userDto.getEmail() != null){
                checkEmailUnique(userDto.getEmail());
            }
            return userStorage.updateUser(userDto);
        } else {
            log.info("Не найден пользователь с id: " + userId);
            throw new UserNotFoundException("id");
        }
    }

    @Override
    public UserDto getById(Long userId) {
        return userStorage.getById(userId).get();
    }

    @Override
    public void deleteById(Long userId) {
        userStorage.deleteById(userId);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    private void checkEmailUnique(String email){
        List<String> emails = userStorage.getAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        if(emails.contains(email)){
            log.info("Пользователь с email " + email + "уже зарегистрирован в системе");
            throw new ValidationException("email");
        }
    }

    private void checkNegativeUserId(Long userId){
        if(userId <= 0){
            log.info("Запрос пользователя с неверным id: " + userId);
            throw new IncorrectParameterException("id");
        }
    }
}