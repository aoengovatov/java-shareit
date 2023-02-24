package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<BookingOutDto> getBookings(@RequestHeader(SHARER_USER_ID) long userId,
                                           @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        BookingStatus state = BookingStatus.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
        return bookingService.getAllByUser(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingOutDto getByUser(@RequestHeader(SHARER_USER_ID) long userId, @PathVariable long bookingId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> getAllBookingItemByUser(@RequestHeader(SHARER_USER_ID) long ownerId,
                                      @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
        BookingStatus state = BookingStatus.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
        return bookingService.getAllBookingItemByOwner(ownerId, state);
    }

    @PostMapping
    public BookingOutDto create(@RequestHeader(SHARER_USER_ID) long userId,
                                @Validated({Create.class}) @RequestBody BookingCreateDto dto) {
        return bookingService.create(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutDto bookingConfirm(@RequestHeader(SHARER_USER_ID) long userId,
                                        @PathVariable long bookingId, @RequestParam String approved) {
        return bookingService.confirm(bookingId, userId, approved);
    }
}
