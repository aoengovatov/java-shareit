package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerIT {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @SneakyThrows
    @Test
    void getAll_whenInvoked_thenReturnStatusOk() {
        long userId = 0L;
        BookingStatus state = BookingStatus.ALL;
        when(bookingService.getAllByUser(userId, state, 0, 10))
                .thenReturn(List.of(new BookingOutDto()));

        mockMvc.perform(get("/bookings")
                        .header(SHARER_USER_ID, userId)
                        .param("from", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService, times(1))
                .getAllByUser(userId, state, 0, 10);
    }

    @SneakyThrows
    @Test
    void getAll_whenPaginationFromIsNegative_thenReturnStatusBadRequest() {
        long userId = 0L;

        mockMvc.perform(get("/bookings")
                        .header(SHARER_USER_ID, userId)
                        .param("from", "-2")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAll_whenPaginationSizeIsNegative_thenReturnStatusBadRequest() {
        long userId = 0L;

        mockMvc.perform(get("/bookings")
                        .header(SHARER_USER_ID, userId)
                        .param("from", "0")
                        .param("size", "-1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAll_whenPaginationSizeIsNull_thenReturnStatusBadRequest() {
        long userId = 0L;

        mockMvc.perform(get("/bookings")
                        .header(SHARER_USER_ID, userId)
                        .param("from", "0")
                        .param("size", "0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getByUser_whenInvoked_thenReturnStatusOk() {
        long userId = 0L;
        long bookingId = 0L;
        when(bookingService.getById(bookingId, userId)).thenReturn(new BookingOutDto());

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(SHARER_USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getById(bookingId, userId);
    }

    @SneakyThrows
    @Test
    void getAllBookingItemByUser_whenInvoked_thenReturnStatusOk() {
        long userId = 0L;
        long bookingId = 0L;
        BookingStatus state = BookingStatus.ALL;
        when(bookingService.getAllBookingItemByOwner(userId, state, 0, 10))
                .thenReturn(List.of(new BookingOutDto()));

        mockMvc.perform(get("/bookings/owner")
                        .header(SHARER_USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService, times(1))
                .getAllBookingItemByOwner(userId, state, 0, 10);
    }

    @SneakyThrows
    @Test
    void create_whenInvoked_thenReturnStatusOk() {
        long userId = 0L;
        BookingCreateDto bookingDto = new BookingCreateDto(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), 2L);
        when(bookingService.create(any(), eq(userId))).thenReturn(new BookingOutDto());

        mockMvc.perform(post("/bookings")
                        .header(SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService, times(1)).create(any(), eq(userId));
    }

    @SneakyThrows
    @Test
    void confirm_whenInvoked_thenReturnStatusOk() {
        long userId = 0L;
        long bookingId = 0L;
        String approved = "true";
        when(bookingService.confirm(bookingId, userId, approved)).thenReturn(new BookingOutDto());

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header(SHARER_USER_ID, userId)
                        .param("approved", "true"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService, times(1)).confirm(bookingId, userId, approved);
    }
}