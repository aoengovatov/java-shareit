package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentCreateDtoTest {

    @Autowired
    private JacksonTester<CommentCreateDto> json;

    @Test
    void testCommentCreateDto() throws Exception {
        CommentCreateDto commentCreateDto = new CommentCreateDto(
                "Some text");

        JsonContent<CommentCreateDto> result = json.write(commentCreateDto);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Some text");
    }

}