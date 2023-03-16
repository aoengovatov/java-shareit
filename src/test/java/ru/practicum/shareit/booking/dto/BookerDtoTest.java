package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookerDtoTest {

    @Autowired
    private JacksonTester<BookerDto> json;

    @Test
    void testBookerDto() throws Exception {
        BookerDto bookerDto = new BookerDto(
                1L);

        JsonContent<BookerDto> result = json.write(bookerDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }
}