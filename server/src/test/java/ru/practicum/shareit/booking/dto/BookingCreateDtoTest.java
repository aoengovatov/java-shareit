package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingCreateDtoTest {

    @Autowired
    private JacksonTester<BookingCreateDto> json;

    @Test
    void testBookingCreateDto() throws Exception {
        BookingCreateDto bookingCreateDto = new BookingCreateDto(
                1L,
                LocalDateTime.of(2023, 2, 12, 12, 29),
                LocalDateTime.of(2023, 2, 16, 12, 29),
                1L);

        JsonContent<BookingCreateDto> result = json.write(bookingCreateDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo("2023-02-12T12:29:00");
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo("2023-02-16T12:29:00");
    }
}