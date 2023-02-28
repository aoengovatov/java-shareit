package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.common.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<BookingOutDto> getAll(@RequestHeader(SHARER_USER_ID) long userId,
                                       @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                       @PositiveOrZero @RequestParam (name = "from", defaultValue = "0") Integer from,
                                       @Positive @RequestParam (name = "size", defaultValue = "10") Integer size) {
        return bookingService.getAllByUser(userId, checkState(stateParam), from, size);
    }

    @GetMapping("/{bookingId}")
    public BookingOutDto getByUser(@RequestHeader(SHARER_USER_ID) long userId, @PathVariable long bookingId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> getAllBookingItemByUser(@RequestHeader(SHARER_USER_ID) long ownerId,
                                      @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                      @PositiveOrZero @RequestParam (name = "from", defaultValue = "0") Integer from,
                                      @Positive @RequestParam (name = "size", defaultValue = "10") Integer size) {
        return bookingService.getAllBookingItemByOwner(ownerId, checkState(stateParam), from, size);
    }

    @PostMapping
    public BookingOutDto create(@RequestHeader(SHARER_USER_ID) long userId,
                                @Validated({Create.class}) @RequestBody BookingCreateDto dto) {
        return bookingService.create(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutDto confirm(@RequestHeader(SHARER_USER_ID) long userId,
                                        @PathVariable long bookingId, @RequestParam String approved) {
        return bookingService.confirm(bookingId, userId, approved);
    }

    private static BookingStatus checkState(String stateParam) {
        BookingStatus state = BookingStatus.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
        return state;
    }
}
