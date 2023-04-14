package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentOutDtoTest {

    @Autowired
    private JacksonTester<CommentOutDto> json;

    @Test
    void testItemAnswerOutDto() throws Exception {
        CommentOutDto commentOutDto = new CommentOutDto(
                1L,
                "Some text",
                "Author name",
                LocalDateTime.of(2023, 2, 12, 12, 29));

        JsonContent<CommentOutDto> result = json.write(commentOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Some text");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Author name");
        assertThat(result).extractingJsonPathValue("$.created").isEqualTo("2023-02-12T12:29:00");
    }

}