package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);

    Item update(Item item);

    Item getById(Long itemId);

    List<Item> getAll();

    List<Item> getAllByUser(long userId);

    List<Item> getSearch(String toLowerCase);
}