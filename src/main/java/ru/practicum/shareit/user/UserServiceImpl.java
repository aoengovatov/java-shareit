package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        log.info("Добавлен новый user с id: {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, Long userId) {
        User userUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Не найден пользователь с id: " + userId));
        User user = UserMapper.toUser(userDto);
        if (user.getName() != null && !user.getName().isBlank()) {
            userUpdate.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            userUpdate.setEmail(user.getEmail());
        }
        userRepository.save(userUpdate);
        return UserMapper.toUserDto(userUpdate);
    }

    @Override
    public UserDto getById(Long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Не найден пользователь с id: " + userId)));
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}