package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    BookingController bookingController;

    @Test
    void getAll_whenInvoked_thenResponseStatusOkAndReturnedCollectionBookingsInBody() {
        long userId = 0L;
        BookingStatus stateParam = BookingStatus.ALL;
        List<BookingOutDto> expectedBooking = List.of(new BookingOutDto());
        when(bookingService.getAllByUser(userId, stateParam, 0, 10)).thenReturn(expectedBooking);

        ResponseEntity<List<BookingOutDto>> response = bookingController.getAll(userId, "ALL", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooking, response.getBody());
    }

    @Test
    void getAll_whenStateIsNull_thenReturnedIllegalArgumentException() {
        long userId = 0L;

        assertThrows(IllegalArgumentException.class, () -> bookingController.getAll(userId, null, 0, 10));
    }

    @Test
    void getByUser_whenInvoked_thenResponseStatusOkAndReturnedBookingInBody() {
        long userId = 0L;
        long bookingId = 0L;
        BookingOutDto expectedBooking = new BookingOutDto();
        when(bookingService.getById(bookingId, userId)).thenReturn(expectedBooking);

        ResponseEntity<BookingOutDto> response = bookingController.getByUser(bookingId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooking, response.getBody());
    }

    @Test
    void getAllBookingItemByUser_whenInvoked_thenResponseStatusOkAndReturnedCollectionBookingsInBody() {
        long ownerId = 0L;
        BookingStatus stateParam = BookingStatus.ALL;
        List<BookingOutDto> expectedBooking = List.of(new BookingOutDto());
        when(bookingService.getAllBookingItemByOwner(ownerId, stateParam, 0, 10)).thenReturn(expectedBooking);

        ResponseEntity<List<BookingOutDto>> response = bookingController.getAllBookingItemByUser(ownerId,
                "ALL", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooking, response.getBody());
    }

    @Test
    void create_whenInvoked_thenResponseStatusOkAndReturnedBookingInBody() {
        long userId = 0L;
        BookingOutDto expectedBooking = new BookingOutDto();
        when(bookingService.create(any(), eq(userId))).thenReturn(expectedBooking);

        ResponseEntity<BookingOutDto> response = bookingController.create(eq(userId), any());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooking, response.getBody());
    }

    @Test
    void confirm_whenInvoked_thenReturnedBookingInBody() {
        long userId = 0L;
        long bookingId = 0L;
        String approved = "true";
        BookingOutDto expectedBooking = new BookingOutDto();
        when(bookingService.confirm(bookingId, userId, approved)).thenReturn(expectedBooking);

        ResponseEntity<BookingOutDto> response = bookingController.confirm(userId, bookingId, approved);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooking, response.getBody());
    }
}