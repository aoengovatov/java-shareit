package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BookingOutDto create(BookingCreateDto dto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Не найден User с id: " + userId));
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Не найден Item с id: " + dto.getItemId()));
        checkData(dto);
        checkBooker(item, userId);
        checkItemAvailable(item);
        ;
        return BookingMapper.toBookingOutDto(bookingRepository.save(
                BookingMapper.toBooking(dto, user, item, BookingStatus.WAITING)));
    }

    @Override
    public BookingOutDto getById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Не найден Booking с id: " + bookingId));
        checkUserIdForBooking(booking, userId);
        return BookingMapper.toBookingOutDto(booking);
    }

    @Override
    @Transactional
    public BookingOutDto confirm(long bookingId, long userId, String approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Не найден Booking с id: " + bookingId));
        if (booking.getItem().getOwner().getId() != userId) {
            log.info("Неверный userId для BookingId: {}", bookingId);
            throw new UserNotFoundException("Error Booking creator userId");
        }
        if (approved.equals("true") && !booking.getStatus().equals(BookingStatus.APPROVED)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else if (approved.equals("false") && !booking.getStatus().equals(BookingStatus.REJECTED)) {
            booking.setStatus(BookingStatus.REJECTED);
        } else {
            log.info("неверный параметр approved");
            throw new BadParameterException("Error approved");
        }
        return BookingMapper.toBookingOutDto(booking);
    }

    @Override
    public List<BookingOutDto> getAllByUser(long userId, BookingStatus state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Не найден User с id: " + userId));
        List<Booking> bookings = Collections.emptyList();
        switch (state) {
            case ALL:
                bookings = bookingRepository.getAllByUser(userId);
                break;
            case FUTURE:
                bookings = bookingRepository.getAllByUserStateFuture(userId);
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingRepository.getAllByUserState(userId, state);
                break;
            case CURRENT:
                bookings = bookingRepository.getAllByUserStateCurrent(userId);
                break;
            case PAST:
                bookings = bookingRepository.getAllByUserStatePast(userId);
                break;
        }
        return bookings.stream()
                .map(BookingMapper::toBookingOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingOutDto> getAllBookingItemByOwner(long ownerId, BookingStatus state) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Не найден User с id: " + ownerId));
        List<Booking> bookings = Collections.emptyList();
        switch (state) {
            case ALL:
                bookings = bookingRepository.getAllBookingItemByOwner(ownerId);
                break;
            case FUTURE:
                bookings = bookingRepository.getAllBookingItemByOwnerSortFuture(ownerId);
                break;
            case WAITING:
                bookings = bookingRepository.getAllBookingItemByOwnerSortWaiting(ownerId);
                break;
            case REJECTED:
                bookings = bookingRepository.getAllBookingItemByOwnerSortRejected(ownerId);
                break;
            case CURRENT:
                bookings = bookingRepository.getAllBookingItemByOwnerSortCurrent(ownerId);
                break;
            case PAST:
                bookings = bookingRepository.getAllBookingItemByOwnerSortPast(ownerId);
                break;
        }
        return bookings.stream()
                .map(BookingMapper::toBookingOutDto)
                .collect(Collectors.toList());
    }

    private static boolean checkData(BookingCreateDto dto) {
        if (dto.getStart().isBefore(dto.getEnd())) {
            return true;
        } else {
            log.info("Дата начала бронирования должна быть раньше даты окончания бронирования");
            throw new BadParameterException("Error Booking data");
        }
    }

    private static boolean checkBooker(Item item, long userId) {
        if (item.getOwner().getId() != userId) {
            return true;
        } else {
            log.info("Создатель Item-a и Booker не должны совпадать");
            throw new IncorrectParameterException("Error Creator and Booker ID");
        }
    }

    private static boolean checkItemAvailable(Item item) {
        if (item.getAvailable()) {
            return true;
        } else {
            log.info("Item недоступен для бронирования");
            throw new BadParameterException("Error Item Status");
        }
    }

    private static boolean checkUserIdForBooking(Booking booking, long userId) {
        if (booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId) {
            return true;
        } else {
            log.info("Неверный userId для BookingId: {}", booking.getId());
            throw new BookingNotFoundException("Error Booking userId");
        }
    }
}