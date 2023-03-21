package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingItemDtoTest {

    @Autowired
    private JacksonTester<BookingItemDto> json;

    @Test
    void testBookingItemDto() throws Exception {
        BookingItemDto bookingItemDto = new BookingItemDto(
                1L,
                "BookingName");

        JsonContent<BookingItemDto> result = json.write(bookingItemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("BookingName");
    }
}