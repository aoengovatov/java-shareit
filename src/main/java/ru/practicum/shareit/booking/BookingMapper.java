package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {

    public static Booking toBooking(BookingCreateDto dto, User user, Item item) {
        return new Booking(dto.getId(), dto.getStart(), dto.getEnd(),
                item, user, dto.getStatus());
    }

    public static BookingOutDto toBookingOutDto(Booking booking) {
        return new BookingOutDto(booking.getId(), booking.getStart(), booking.getEnd(),
                booking.getStatus(), new BookerDto(booking.getBooker().getId()),
                new BookingItemDto(booking.getItem().getId(), booking.getItem().getName()));
    }
}
