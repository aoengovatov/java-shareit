package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(SHARER_USER_ID) long userId,
                                         @Valid @RequestBody ItemRequestDto dto) {
        log.info("Создание request: {}, userId: {}", dto, userId);
        return itemRequestClient.create(dto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByRequestor(@RequestHeader(SHARER_USER_ID) long userId) {
        log.info("Запрос списка requests пользователя с userId: {}", userId);
        return itemRequestClient.getAllByRequestor(userId);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(SHARER_USER_ID) long userId,
                                          @PathVariable long requestId) {
        log.info("Запрос request с id: {} от пользователя с userId: {}", requestId, userId);
        return itemRequestClient.getAllByRequestId(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(SHARER_USER_ID) long userId,
                                         @PositiveOrZero @RequestParam (name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam (name = "size", defaultValue = "10") Integer size) {
        log.info("Запрос списка requests. UserId: {}, from: {}, size: {}", userId, from, size);
        return itemRequestClient.getAll(userId, from, size);
    }
}