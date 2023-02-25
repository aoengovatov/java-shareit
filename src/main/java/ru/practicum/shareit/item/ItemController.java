package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    List<ItemOutDto> getAll(@RequestHeader(SHARER_USER_ID) long userId) {
        if (userId > 0) {
            return itemService.getAllByUser(userId);
        }
        return itemService.getAll();
    }

    @GetMapping("/{itemId}")
    ItemOutDto getAll(@RequestHeader(SHARER_USER_ID) long userId,
                      @PathVariable Long itemId) {
        return itemService.getById(itemId, userId);
    }

    @PostMapping
    ItemOutDto create(@RequestHeader(SHARER_USER_ID) long userId,
                      @Validated({Create.class}) @RequestBody ItemCreateDto dto) {
        return itemService.create(dto, userId);
    }

    @PostMapping("/{itemId}/comment")
    CommentOutDto addComment(@RequestHeader(SHARER_USER_ID) long userId,
                             @PathVariable Long itemId,
                             @Validated({Create.class}) @RequestBody CommentCreateDto dto) {
        return itemService.addComment(dto, userId, itemId);
    }

    @PatchMapping ("/{itemId}")
    ItemOutDto update(@RequestHeader(SHARER_USER_ID) long userId,
                      @PathVariable Long itemId,
                      @Validated({Update.class}) @RequestBody ItemOutDto itemOutDto) {

        return itemService.update(itemOutDto, itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemOutDto> search(@RequestParam String text) {
        return itemService.getSearch(text.toLowerCase());
    }
}