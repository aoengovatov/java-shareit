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

import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements  ItemService {

    final
    ItemStorage itemStorage;
    UserStorage userStorage;

    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemCreateDto create(ItemCreateDto itemDto, long userId) {
        checkUserId(userId);
        User owner = UserMapper.toUser(userStorage.getById(userId).get());
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
        return ItemMapper.toItemDto(itemStorage.getById(itemId).get());
    }

    @Override
    public List<ItemDto> getAllByUser(long userId) {
        return itemStorage.getAllByUser(userId);
    }

    private void checkUserId(Long userId) {
        if (userId == null || userId <= 0) {
            log.info("Пользователь с id " + userId + " не найден");
            throw new ValidationException("userId");
        }
    }

    private void checkItemId(Long itemId) {
        if (itemId == null || itemId <= 0) {
            log.info("Вещь с id " + itemId + " не найдена");
            throw new ValidationException("itemId");
        }
    }
}