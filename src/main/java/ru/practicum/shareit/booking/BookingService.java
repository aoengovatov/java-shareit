package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

@Service
public interface BookingService {
    BookingOutDto create(BookingCreateDto dto, long userId);

    BookingOutDto getById(long bookingId, long userId);

    BookingOutDto confirm(long bookingId, long userId, String approved);

    List<BookingOutDto> getAllByUser(long userId, BookingStatus state,
                                     Integer from, Integer size);

    List<BookingOutDto> getAllBookingItemByOwner(long ownerId, BookingStatus state,
                                                 Integer from, Integer size);
}
