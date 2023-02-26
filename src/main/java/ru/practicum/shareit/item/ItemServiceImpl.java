package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
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

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

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
        Item item = itemRepository.save(ItemMapper.toItemFromCreateDto(dto, owner));
        log.info("Создан новый item с id: {}", item.getId());
        return ItemMapper.toItemOutDto(item);
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
        return ItemMapper.toItemOutDto(itemUpdate);
    }

    @Override
    public List<ItemOutDto> getAll() {
        log.info("Запрос всех Item");
        return itemRepository.findAll().stream()
                .map(ItemMapper::toItemOutDto)
                .collect(toList());
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
                .collect(toList());
        itemOut.setComments(comments);
        log.info("Запрос Item-a с id: {}", itemId);
        return itemOut;
    }

    @Override
    public List<ItemOutDto> getAllByUser(long userId) {
        List<ItemOutDto> items = itemRepository.getAllByUser(userId).stream()
                .map(ItemMapper::toItemOutDto)
                .collect(toList());
        log.info("Запрос всех Item для User с id: {}", userId);
        return getBookingInfo(items);
    }

    @Override
    public List<ItemOutDto> getSearch(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        log.info("Поиск в Item по text: {}", text);
        return itemRepository.getSearch(text).stream()
                .map(ItemMapper::toItemOutDto)
                .collect(toList());
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
        dto.setCreated(now());
        dto.setItemId(itemId);
        log.info("Добавлен комментарий для Item с id: {} от пользователя с id {}", itemId, bookerId);
        return CommentMapper.toCommentOutDto(commentRepository.save(CommentMapper.toComment(dto)));
    }

    private List<ItemOutDto> getBookingInfo(List<ItemOutDto> items) {
//        List<Long> itemIds = items.stream()
//                .map(ItemOutDto::getId)
//                .collect(toList());
//        List<ItemOutDto> itemOut = new ArrayList<>();
//        Map<Item, List<Booking>> bookingsItem = bookingRepository.getAllBookingByItem(itemIds,
//                        BookingStatus.APPROVED, Sort.by(Sort.Direction.DESC, "start")).stream()
//                        .collect(groupingBy(Booking::getItem, toList()));
//        LocalDateTime now = LocalDateTime.now();
//        for (Item item : bookingsItem.keySet()) {
//            List<Booking> bookings = bookingsItem.get(item);
//            for (Booking booking : bookings) {
//                if (booking.getStart().isAfter(now)) {
//                    ItemOutDto itemOutDto = ItemMapper.toItemOutDto(item);
//                    itemOutDto.setNextBooking(new BookingItemDto(booking.getId(), booking.getBooker().getId()));
//                    itemOut.add(itemOutDto);
//                }
//                if (booking.getEnd().isBefore(now)) {
//                    ItemOutDto itemOutDto = ItemMapper.toItemOutDto(item);
//                    itemOutDto.setLastBooking(new BookingItemDto(booking.getId(), booking.getBooker().getId()));
//                    itemOut.add(itemOutDto);
//                }
//            }
//        }
//        return itemOut;

        List<Long> itemIds = items.stream()
                .map(ItemOutDto::getId)
                .collect(toList());
        List<Booking> allBookings = bookingRepository.getAllBookingByItem(itemIds,
                BookingStatus.APPROVED, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();
        for (ItemOutDto item : items) {
            List<Booking> bookings = allBookings.stream()
                    .filter(b -> b.getItem().getId() == item.getId())
                    .collect(Collectors.toList());
            if (!bookings.isEmpty()) {
                for (Booking booking : bookings) {
                    if (booking.getEnd().isBefore(now)) {
                        item.setLastBooking(new BookingItemDto(booking.getId(),
                                booking.getBooker().getId()));
                    }
                    if (booking.getStart().isAfter(now)) {
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