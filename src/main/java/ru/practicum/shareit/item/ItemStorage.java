package ru.practicum.shareit.item;


import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    ItemCreateDto create(ItemCreateDto itemDto, User owner);

    ItemUpdateDto update(ItemUpdateDto itemDto, Long userId);

    Item getById(Long itemId);

    List<ItemDto> getAll();

    List<ItemDto> getAllByUser(long userId);

    List<ItemDto> getSearch(String toLowerCase);
}