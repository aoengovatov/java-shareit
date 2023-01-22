package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";
    @Autowired
    ItemService itemService;

    @GetMapping
    List<ItemDto> getAll(@RequestHeader(value = SHARER_USER_ID, defaultValue = "0") long userId){
        if(userId > 0){
            return itemService.getAllByUser(userId);
        }
        return itemService.getAll();
    }

    @GetMapping("/{itemId}")
    ItemDto getAll(@PathVariable Long itemId){
        return itemService.getById(itemId);
    }

    @PostMapping
    ItemCreateDto create(@RequestHeader(SHARER_USER_ID) long userId,
                        @Valid @RequestBody ItemCreateDto itemDto) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping ("/{itemId}")
    ItemUpdateDto update(@RequestHeader(SHARER_USER_ID) long userId,
                             @PathVariable Long itemId,
                             @RequestBody ItemUpdateDto itemDto) {

        return itemService.update(itemDto, itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text){
        return itemService.getSearch(text);
    }
}