package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    UserDto getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @PostMapping
    UserDto create(@Validated({Create.class}) @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping ("/{userId}")
    UserDto update(@PathVariable Long userId,
                   @Validated({Update.class}) @RequestBody UserDto userDto) {
        return userService.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    void deleteById(@PathVariable Long userId) {
        userService.deleteById(userId);
    }
}
