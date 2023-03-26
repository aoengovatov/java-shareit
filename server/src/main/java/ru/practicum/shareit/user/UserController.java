package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{userId}")
    ResponseEntity<UserDto> getById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @PostMapping
    ResponseEntity<UserDto> create(@Validated({Create.class}) @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.create(userDto));
    }

    @PatchMapping ("/{userId}")
    ResponseEntity<UserDto> update(@PathVariable Long userId,
                   @Validated({Update.class}) @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.update(userDto, userId));
    }

    @DeleteMapping("/{userId}")
    void deleteById(@PathVariable Long userId) {
        userService.deleteById(userId);
    }
}
