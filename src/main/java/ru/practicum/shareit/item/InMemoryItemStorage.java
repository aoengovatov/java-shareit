package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private long itemId = 0;

    @Override
    public Item create(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item itemUpdate) {
        Item item = items.get(itemUpdate.getId());
        if (checkItemName(itemUpdate)) {
            item.setName(itemUpdate.getName());
        }
        if (checkItemDescription(itemUpdate)) {
            item.setDescription(itemUpdate.getDescription());
        }
        if (checkItemAvailable(itemUpdate, item)) {
            item.setAvailable(itemUpdate.getAvailable());
        }
        items.put(item.getId(), item);
        log.info("Обновление item с id: {}", item.getId());
        return item;
    }

    @Override
    public Item getById(Long itemId) {
        if (items.containsKey(itemId)) {
            log.info("Найден item с id: {}", itemId);
            return items.get(itemId);
        } else {
            log.info("Не найден item с id: {}", itemId);
            throw new ItemNotFoundException("itemId");
        }
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getAllByUser(long userId) {
        return new ArrayList<>(items.values()).stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getSearch(String text) {
        return new ArrayList<>(items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList()));
    }

    private long generateId() {
        return ++itemId;
    }

    private boolean checkItemName(Item item) {
        if (item.getName() != null) {
            return !item.getName().isBlank();
        }
        return false;
    }

    private boolean checkItemDescription(Item item) {
        if (item.getDescription() != null) {
            return !item.getDescription().isBlank();
        }
        return false;
    }

    private boolean checkItemAvailable(Item itemUpdate, Item item) {
        return itemUpdate.getAvailable() != null && itemUpdate.getAvailable() != item.getAvailable();
    }
}