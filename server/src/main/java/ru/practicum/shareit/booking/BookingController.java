package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BookingOutDto>> getAll(@RequestHeader(SHARER_USER_ID) long userId,
                                       @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                       @RequestParam (name = "from", defaultValue = "0") Integer from,
                                       @RequestParam (name = "size", defaultValue = "10") Integer size) {
        BookingStatus state = checkState(stateParam);
        return ResponseEntity.ok(bookingService.getAllByUser(userId, state, from, size));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingOutDto> getByUser(@RequestHeader(SHARER_USER_ID) long userId, @PathVariable long bookingId) {
        return ResponseEntity.ok(bookingService.getById(bookingId, userId));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingOutDto>> getAllBookingItemByUser(@RequestHeader(SHARER_USER_ID) long ownerId,
                                      @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                      @RequestParam (name = "from", defaultValue = "0") Integer from,
                                      @RequestParam (name = "size", defaultValue = "10") Integer size) {
        BookingStatus state = checkState(stateParam);
        return ResponseEntity.ok(bookingService.getAllBookingItemByOwner(ownerId, state, from, size));
    }

    @PostMapping
    public ResponseEntity<BookingOutDto> create(@RequestHeader(SHARER_USER_ID) long userId,
                                @RequestBody BookingCreateDto dto) {
        return ResponseEntity.ok(bookingService.create(dto, userId));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingOutDto> confirm(@RequestHeader(SHARER_USER_ID) long userId,
                                                 @PathVariable long bookingId, @RequestParam String approved) {
        return ResponseEntity.ok(bookingService.confirm(bookingId, userId, approved));
    }

    private static BookingStatus checkState(String stateParam) {
        BookingStatus state = BookingStatus.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
        return state;
    }
}
