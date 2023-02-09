package ru.practicum.shareit.user;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserStorage {
    User create(User user);

    User update(User user);

    List<User> getAll();

    User getById(Long userId);

    void deleteById(Long userId);
}
