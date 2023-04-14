package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private BookingClient bookingClient;

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
    void getAll_whenBookingDataError_thenReturnStatusIsBadRequest() {
        long userId = 0L;

        BookingDto bookingDto = new BookingDto(0L, LocalDateTime.now(),
                LocalDateTime.now().minusDays(1), 2L);

        mockMvc.perform(post("/bookings")
                        .header(SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}