package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;

import java.util.List;

public interface ItemService {
    ItemOutDto create(ItemCreateDto dto, long userId);

    ItemOutDto update(ItemOutDto itemOutDto, Long itemId, Long userId);

    List<ItemOutDto> getAll();

    ItemOutDto getById(long itemId, long userId);

    List<ItemOutDto> getAllByUser(long userId);

    List<ItemOutDto> getSearch(String text);

    CommentOutDto addComment(CommentCreateDto dto, long userId, long itemId);
}