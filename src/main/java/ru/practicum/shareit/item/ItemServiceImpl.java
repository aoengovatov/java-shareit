package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BadParameterException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements  ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ItemOutDto create(ItemCreateDto dto, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Не найден User с id: " + userId));
        return ItemMapper.toItemOutDto(itemRepository.save(ItemMapper.toItemFromCreateDto(dto, owner)));
    }

    @Override
    @Transactional
    public ItemOutDto update(ItemOutDto dto, Long itemId, Long userId) {
        Item itemUpdate = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Не найден Item с id: " + itemId));
        if (itemUpdate.getOwner().getId() != userId) {
            log.info("У пользователя с id: {} не найден Item c id: {}", userId, itemId);
            throw new ItemNotFoundException("ItemId");
        }
        dto.setId(itemId);
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Не найден User с id: " + userId));

        Item item = ItemMapper.toItemFromOutDto(dto, owner);
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
        return ItemMapper.toItemOutDto(itemRepository.save(itemUpdate));
    }

    @Override
    public List<ItemOutDto> getAll() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::toItemOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemOutDto getById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Не найден Item с id: " + itemId));
        ItemOutDto itemOut = ItemMapper.toItemOutDto(item);
        if (item.getOwner().getId() == userId) {
            List<ItemOutDto> items = new ArrayList<>();
            items.add(ItemMapper.toItemOutDto(item));
            itemOut = getBookingInfo(items).get(0);
        }
        List<CommentOutDto> comments = commentRepository.getCommentByItemId(itemId).stream()
                .map(CommentMapper::toCommentOutDto)
                .collect(Collectors.toList());
        if (!comments.isEmpty()) {
            itemOut.setComments(comments);
        } else {
            itemOut.setComments(Collections.emptyList());
        }
        return itemOut;
    }

    @Override
    public List<ItemOutDto> getAllByUser(long userId) {
        List<ItemOutDto> items = itemRepository.getAllByUser(userId).stream()
                .map(ItemMapper::toItemOutDto)
                .collect(Collectors.toList());
        return getBookingInfo(items);
    }

    @Override
    public List<ItemOutDto> getSearch(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.getSearch(text).stream()
                .map(ItemMapper::toItemOutDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentOutDto addComment(CommentCreateDto dto, long bookerId, long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Не найден Item с id: " + itemId));
        List<Booking> bookings = bookingRepository.getAllByBookerAndItemId(bookerId, itemId);
        if (bookings.isEmpty()) {
            log.info("У пользователя с id: {} не найдена аренда Item c id: {}", bookerId, itemId);
            throw new BadParameterException("BookerId not found");
        }
        for (Booking booking : bookings) {
            dto.setAuthorName(booking.getBooker().getName());
        }
        dto.setCreated(LocalDateTime.now());
        dto.setItemId(itemId);
        return CommentMapper.toCommentOutDto(commentRepository.save(CommentMapper.toComment(dto)));
    }

    private List<ItemOutDto> getBookingInfo(List<ItemOutDto> items) {
        List<Long> itemIds = items.stream()
                .map(ItemOutDto::getId)
                .collect(Collectors.toList());
        List<Booking> allBookings = bookingRepository.getAllBookingByItem(itemIds);
        for (ItemOutDto item : items) {
            List<Booking> bookings = allBookings.stream()
                    .filter(b -> b.getItem().getId() == item.getId())
                    .collect(Collectors.toList());
            if (!bookings.isEmpty()) {
                for (Booking booking : bookings) {
                    if (booking.getEnd().isBefore(LocalDateTime.now())) {
                        item.setLastBooking(new BookingItemDto(booking.getId(),
                                booking.getBooker().getId()));
                    }
                    if (booking.getStart().isAfter(LocalDateTime.now())) {
                        item.setNextBooking(new BookingItemDto(booking.getId(),
                                booking.getBooker().getId()));
                    }
                }
            }
        }
        return items;
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