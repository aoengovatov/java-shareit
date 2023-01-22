package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InMemoryItemStorage implements ItemStorage{

    private final Map<Long, Item> items;
    private long itemId = 0;

    public InMemoryItemStorage(Map<Long, Item> items) {
        this.items = items;
    }

    @Override
    public ItemCreateDto create(ItemCreateDto itemDto, User owner) {
        itemDto.setId(generateId());
        Item item = ItemMapper.toItemFromItemCreateDto(itemDto, owner);
        items.put(item.getId(), item);
        return ItemMapper.toItemCreateDto(item);
    }

    @Override
    public ItemUpdateDto update(ItemUpdateDto itemDto, Long userId) {
        if(checkItemId(itemDto)){
            Item item = items.get(itemDto.getId());
            if(item.getOwner().getId() == userId){
                if(checkItemName(itemDto)){
                    item.setName(itemDto.getName());
                }
                if(checkItemDescription(itemDto)){
                    item.setDescription(itemDto.getDescription());
                }
                if(checkItemAvailable(itemDto, item)){
                    item.setAvailable(itemDto.getAvailable());
                }
                items.put(item.getId(), item);
                log.info("Обновление item с id: " + item.getId());
                return ItemMapper.toItemUpdateDto(item);
            } else {
                log.info("Обновление item c id: " + itemDto.getId() + " с неверным userId: " + userId);
                throw new UserNotFoundException("userId");
            }
        }
        log.info("Не найден item с id: " + itemDto.getId());
        throw new ItemNotFoundException("itemId");
    }

    @Override
    public Optional<Item> getById(Long itemId) {
        if(items.containsKey(itemId)){
            log.info("Найден item с id: " + itemId);
            return Optional.of(items.get(itemId));
        } else {
            log.info("Не найден item с id: " + itemId);
            throw new ItemNotFoundException("itemId");
        }
    }

    @Override
    public List<ItemDto> getAll() {
        return new ArrayList<>(items.values()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAllByUser(long userId) {
        return new ArrayList<>(items.values()).stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        return new ArrayList<>(items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text))
                .filter(item -> item.getAvailable().equals(true))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList()));
    }

    private long generateId(){
        return ++itemId;
    }

    private boolean checkItemId(ItemUpdateDto itemDto){
        if(items.containsKey(itemDto.getId())){
            return true;
        }
        return false;
    }

    private boolean checkItemName(ItemUpdateDto itemDto){
        if(itemDto.getName() != null){
            if(!itemDto.getName().isBlank()){
                return true;
            }
        }
        return false;
    }

    private boolean checkItemDescription(ItemUpdateDto itemDto){
        if(itemDto.getDescription() != null){
            if(!itemDto.getDescription().isBlank()){
                return true;
            }
        }
        return false;
    }

    private boolean checkItemAvailable(ItemUpdateDto itemDto, Item item){
        if(itemDto.getAvailable() != null && itemDto.getAvailable() != item.getAvailable()){
            return true;
        }
        return false;
    }
}