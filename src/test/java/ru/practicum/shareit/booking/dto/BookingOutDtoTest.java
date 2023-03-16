package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingOutDtoTest {

    @Autowired
    private JacksonTester<BookingOutDto> json;

    @Test
    void testBookingOutDto() throws Exception {
        BookerDto bookerDto = new BookerDto(2L);
        BookingItemDto bookingItemDto = new BookingItemDto(1L, "Name");
        BookingOutDto bookingOutDto = new BookingOutDto(
                1L,
                LocalDateTime.of(2023, 2, 12, 12, 29),
                LocalDateTime.of(2023, 2, 16, 12, 29),
                BookingStatus.WAITING,
                bookerDto,
                bookingItemDto);

        JsonContent<BookingOutDto> result = json.write(bookingOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo("2023-02-12T12:29:00");
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo("2023-02-16T12:29:00");
        assertThat(result).extractingJsonPathValue("$.status").isEqualTo("WAITING");
        assertThat(result).extractingJsonPathValue("$.booker.id").isEqualTo(2);
        assertThat(result).extractingJsonPathValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.item.name").isEqualTo("Name");
    }
}