package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemOutDto;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";
    @Autowired
    private ItemService itemService;

    @GetMapping
    ResponseEntity<List<ItemOutDto>> getAll(@RequestHeader(SHARER_USER_ID) long userId) {
        if (userId > 0) {
            return ResponseEntity.ok(itemService.getAllByUser(userId));
        }
        return ResponseEntity.ok(itemService.getAll());
    }

    @GetMapping("/{itemId}")
    ResponseEntity<ItemOutDto> getAll(@RequestHeader(SHARER_USER_ID) long userId,
                      @PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getById(itemId, userId));
    }

    @PostMapping
    ResponseEntity<ItemOutDto> create(@RequestHeader(SHARER_USER_ID) long userId,
                      @Validated({Create.class}) @RequestBody ItemCreateDto dto) {
        return ResponseEntity.ok(itemService.create(dto, userId));
    }

    @PostMapping("/{itemId}/comment")
    ResponseEntity<CommentOutDto> addComment(@RequestHeader(SHARER_USER_ID) long userId,
                             @PathVariable Long itemId,
                             @Validated({Create.class}) @RequestBody CommentCreateDto dto) {
        return ResponseEntity.ok(itemService.addComment(dto, userId, itemId));
    }

    @PatchMapping ("/{itemId}")
    ResponseEntity<ItemOutDto> update(@RequestHeader(SHARER_USER_ID) long userId,
                      @PathVariable Long itemId,
                      @Validated({Update.class}) @RequestBody ItemOutDto itemOutDto) {

        return ResponseEntity.ok(itemService.update(itemOutDto, itemId, userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemOutDto>> search(@RequestParam String text) {
        return ResponseEntity.ok(itemService.getSearch(text));
    }
}