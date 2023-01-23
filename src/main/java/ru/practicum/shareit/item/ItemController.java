package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";
    @Autowired
    private ItemService itemService;

    @GetMapping
    List<ItemDto> getAll(@RequestHeader(SHARER_USER_ID) long userId) {
        if (userId > 0) {
            return itemService.getAllByUser(userId);
        }
        return itemService.getAll();
    }

    @GetMapping("/{itemId}")
    ItemDto getAll(@PathVariable Long itemId) {
        return itemService.getById(itemId);
    }

    @PostMapping
    ItemDto create(@RequestHeader(SHARER_USER_ID) long userId,
                        @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping ("/{itemId}")
    ItemDto update(@RequestHeader(SHARER_USER_ID) long userId,
                             @PathVariable Long itemId,
                   @Validated({Update.class}) @RequestBody ItemDto itemDto) {

        return itemService.update(itemDto, itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.getSearch(text);
    }
}