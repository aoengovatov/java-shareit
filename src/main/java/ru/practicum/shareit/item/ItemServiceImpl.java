package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

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
    public ItemCreateDto create(ItemCreateDto itemDto, long userId) {
        checkUserId(userId);
        User owner = UserMapper.toUser(userStorage.getById(userId));
        return itemStorage.create(itemDto, owner);
    }

    @Override
    public ItemUpdateDto update(ItemUpdateDto itemDto, Long itemId, Long userId) {
        checkUserId(userId);
        checkItemId(itemId);
        itemDto.setId(itemId);
        return itemStorage.update(itemDto, userId);
    }

    @Override
    public List<ItemDto> getAll() {
        return itemStorage.getAll();
    }

    @Override
    public ItemDto getById(Long itemId) {
        return ItemMapper.toItemDto(itemStorage.getById(itemId));
    }

    @Override
    public List<ItemDto> getAllByUser(long userId) {
        return itemStorage.getAllByUser(userId);
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        if (text.isBlank()) {
            return new ArrayList<ItemDto>();
        }
        return itemStorage.getSearch(text.toLowerCase());
    }

    private void checkUserId(Long userId) {
        if (userId == null || userId <= 0) {
            log.info("Пользователь с id {} не найден", userId);
            throw new ValidationException("userId");
        }
    }

    private void checkItemId(Long itemId) {
        if (itemId == null || itemId <= 0) {
            log.info("Вещь с id {} не найдена", itemId);
            throw new ValidationException("itemId");
        }
    }
}