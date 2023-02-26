package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemOutDto;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@UtilityClass
public class ItemMapper {

    public static Item toItemFromCreateDto(ItemCreateDto dto, User owner) {
        return new Item(dto.getId(), dto.getName(), dto.getDescription(), dto.getAvailable(), owner);
    }

    public static Item toItemFromOutDto(ItemOutDto dto, User owner) {
        return new Item(dto.getId(), dto.getName(), dto.getDescription(), dto.getAvailable(), owner);
    }

    public static ItemOutDto toItemOutDto(Item item) {
        return new ItemOutDto(item.getId(), item.getName(),
                item.getDescription(), item.getAvailable());
    }
}