package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    ItemCreateDto create(ItemCreateDto itemDto, long userId);

    ItemUpdateDto update(ItemUpdateDto itemDto, Long itemId, Long userId);

    List<ItemDto> getAll();

    ItemDto getById(Long itemId);

    List<ItemDto> getAllByUser(long userId);
}