package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

public interface BookingService {
    BookingOutDto create(BookingCreateDto dto, long userId);

    BookingOutDto getById(long bookingId, long userId);

    BookingOutDto confirm(long bookingId, long userId, String approved);

    List<BookingOutDto> getAllByUser(long userId, BookingStatus state);

    List<BookingOutDto> getAllBookingItemByOwner(long ownerId, BookingStatus state);
}
