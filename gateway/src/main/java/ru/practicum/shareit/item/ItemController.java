package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @GetMapping
    ResponseEntity<Object> getAll(@RequestHeader(SHARER_USER_ID) long userId) {
        log.info("Запрос списка items");
        return itemClient.getAll(userId);
    }

    @GetMapping("/{itemId}")
    ResponseEntity<Object> getAll(@RequestHeader(SHARER_USER_ID) long userId,
                                      @PathVariable Long itemId) {
        log.info("Запрос item id: {}, user id {}", itemId, userId);
        return itemClient.getById(itemId, userId);
    }

    @PostMapping
    ResponseEntity<Object> create(@RequestHeader(SHARER_USER_ID) long userId,
                                      @Valid @RequestBody ItemDto dto) {
        log.info("Создание item: {}, user id: {}", dto, userId);
        return itemClient.create(dto, userId);
    }

    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> addComment(@RequestHeader(SHARER_USER_ID) long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentDto dto) {
        log.info("Создание comment: {}, user id: {}, для item id: {}", dto, userId, itemId);
        return itemClient.addComment(dto, userId, itemId);
    }

    @PatchMapping ("/{itemId}")
    ResponseEntity<Object> update(@RequestHeader(SHARER_USER_ID) long userId,
                                      @PathVariable Long itemId,
                                      @RequestBody ItemDto dto) {
        log.info("Обновление item: {}, user id: {}", dto, userId);
        return itemClient.update(dto, itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        log.info("Поиск items c : {}", text);
        if (text.isBlank()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        return itemClient.getSearch(text);
    }
}