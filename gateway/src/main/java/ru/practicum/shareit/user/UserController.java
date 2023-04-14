package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Запрос списка users");
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId) {
        log.info("Запрос user id: {}", userId);
        return userClient.getById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated({Create.class}) UserDto userDto) {
        log.info("Создаем user {}", userDto);
        return userClient.create(userDto);
    }

    @PatchMapping ("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Long userId,
                                   @Validated({Update.class}) @RequestBody UserDto userDto) {
        log.info("Обновление user id: {}", userId);
        return userClient.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        log.info("Удаление user id: {}", userId);
        userClient.deleteById(userId);
    }
}