package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
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
    ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.create(userDto));
    }

    @PatchMapping ("/{userId}")
    ResponseEntity<UserDto> update(@PathVariable Long userId,
                   @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.update(userDto, userId));
    }

    @DeleteMapping("/{userId}")
    void deleteById(@PathVariable Long userId) {
        userService.deleteById(userId);
    }
}