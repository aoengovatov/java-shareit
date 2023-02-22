package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements  ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, long userId) {
        User owner = userRepository.findById(userId).get();
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, owner)));
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        Item itemUpdate = itemRepository.findById(itemId).
                orElseThrow(() -> new ItemNotFoundException("Не найден Item с id: " + itemId));
        if (itemUpdate.getOwner().getId() != userId) {
            log.info("У пользователя с id: {} не найден Item c id: {}", userId, itemId);
            throw new ItemNotFoundException("ItemId");
        }
        itemDto.setId(itemId);
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Не найден User с id: " + itemId));

        Item item = ItemMapper.toItem(itemDto, owner);
        if (checkItemName(item)) {
            itemUpdate.setName(item.getName());
        }
        if (checkItemDescription(item)) {
            itemUpdate.setDescription(item.getDescription());
        }
        if (checkItemAvailable(item, itemUpdate)) {
            itemUpdate.setAvailable(item.getAvailable());
        }
        log.info("Обновление item с id: {}", item.getId());
        return ItemMapper.toItemDto(itemRepository.save(itemUpdate));
    }

    @Override
    public List<ItemDto> getAll() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(Long itemId) {
        return ItemMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Не найден Item с id: " + itemId)));
    }

    @Override
    public List<ItemDto> getAllByUser(long userId) {
        return itemRepository.getAllByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.getSearch(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
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