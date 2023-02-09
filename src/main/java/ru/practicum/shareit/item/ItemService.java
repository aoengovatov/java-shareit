package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, long userId);

    ItemDto update(ItemDto itemDto, Long itemId, Long userId);

    List<ItemDto> getAll();

    ItemDto getById(Long itemId);

    List<ItemDto> getAllByUser(long userId);

    List<ItemDto> getSearch(String text);
}