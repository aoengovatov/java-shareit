package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements  ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        User owner = userStorage.getById(userId);
        return ItemMapper.toItemDto(itemStorage.create(ItemMapper.toItem(itemDto, owner)));
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        Item itemUpdate = itemStorage.getById(itemId);
        itemDto.setId(itemId);
        User owner = userStorage.getById(userId);
        Item item = ItemMapper.toItem(itemDto, owner);
        if (itemUpdate.getOwner().getId() != userId) {
            log.info("Обновление item c id: {} с неверным userId: {}", itemDto.getId(), userId);
            throw new UserNotFoundException("userId");
        }
        return ItemMapper.toItemDto(itemStorage.update(item));
    }

    @Override
    public List<ItemDto> getAll() {
        return itemStorage.getAll().stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(Long itemId) {
        return ItemMapper.toItemDto(itemStorage.getById(itemId));
    }

    @Override
    public List<ItemDto> getAllByUser(long userId) {
        return itemStorage.getAllByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        if (text.isBlank()) {
            return new ArrayList<ItemDto>();
        }
        return itemStorage.getSearch(text.toLowerCase()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}