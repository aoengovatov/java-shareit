package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemOutDto;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@UtilityClass
public class ItemMapper {

    public static Item toItemFromCreateDto(ItemCreateDto dto, User owner, ItemRequest request) {
        return new Item(dto.getId(), dto.getName(), dto.getDescription(),
                dto.getAvailable(), owner, request);
    }

    public static Item toItemFromOutDto(ItemOutDto dto, User owner, ItemRequest request) {
        return new Item(dto.getId(), dto.getName(), dto.getDescription(), dto.getAvailable(),
                owner, request);
    }

    public static ItemOutDto toItemOutDto(Item item) {
        Long requestId = null;
        if (item.getRequest() != null) {
            requestId = item.getRequest().getId();
        }
        return new ItemOutDto(item.getId(), item.getName(),
                item.getDescription(), item.getAvailable(), requestId);
    }
}