package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Component
public class ItemMapper {

    public static Item toItemFromItemCreateDto(ItemCreateDto itemDto, User owner) {
        return new Item(itemDto.getId(), itemDto.getName(),
                itemDto.getDescription(), itemDto.getAvailable(), owner);
    }

    public static ItemCreateDto toItemCreateDto(Item item) {
        return new ItemCreateDto(item.getId(), item.getName(),
                item.getDescription(), item.getAvailable());
    }

    public static Item toItemFromItemUpdateDto(ItemUpdateDto itemDto, User owner) {
        return new Item(itemDto.getId(), itemDto.getName(),
                itemDto.getDescription(), itemDto.getAvailable(), owner);
    }

    public static ItemUpdateDto toItemUpdateDto(Item item) {
        return new ItemUpdateDto(item.getId(), item.getName(),
                item.getDescription(), item.getAvailable());
    }

    public static Item toItemFromItemDto(ItemDto itemDto, User owner) {
        return new Item(itemDto.getId(), itemDto.getName(),
                itemDto.getDescription(), itemDto.getAvailable(), owner);
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(),
                item.getDescription(), item.getAvailable());
    }
}