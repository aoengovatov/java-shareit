package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(SHARER_USER_ID) long userId,
                                         @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                         @PositiveOrZero @RequestParam (name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam (name = "size", defaultValue = "10") Integer size) {
        BookingStatus state = checkState(stateParam);
        log.info("Запрос списка bookings. UserId: {}, state: {}, from: {}, size: {}", userId, state, from, size);
        return bookingClient.getAllByUser(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getByUser(@RequestHeader(SHARER_USER_ID) long userId, @PathVariable long bookingId) {
        log.info("Запрос списка bookings userId: {}", userId);
        return bookingClient.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingItemByUser(@RequestHeader(SHARER_USER_ID) long ownerId,
                                                          @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                          @PositiveOrZero @RequestParam (name = "from", defaultValue = "0") Integer from,
                                                          @Positive @RequestParam (name = "size", defaultValue = "10") Integer size) {
        BookingStatus state = checkState(stateParam);
        log.info("Запрос списка bookings. OwnerId: {}, state: {}, from: {}, size: {}", ownerId, state, from, size);
        return bookingClient.getAllBookingItemByOwner(ownerId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(SHARER_USER_ID) long userId,
                                                @Valid @RequestBody BookingDto dto) {
        log.info("Создание booking: {} от userId: {}", dto, userId);
        return bookingClient.create(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirm(@RequestHeader(SHARER_USER_ID) long userId,
                                                 @PathVariable long bookingId, @RequestParam String approved) {
        log.info("Изменение статуса booking: {} от userId: {}", bookingId, userId);
        return bookingClient.confirm(bookingId, userId, approved);
    }

    private static BookingStatus checkState(String stateParam) {
        BookingStatus state = BookingStatus.from(stateParam);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + stateParam);
        }
        return state;
    }
}