package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemAnswerOutDtoTest {

    @Autowired
    private JacksonTester<ItemAnswerOutDto> json;

    @Test
    void testItemAnswerOutDto() throws Exception {
        ItemAnswerOutDto itemAnswerOutDto = new ItemAnswerOutDto(
                1L,
                "Itemname",
                "description",
                true,
                1L);

        JsonContent<ItemAnswerOutDto> result = json.write(itemAnswerOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Itemname");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }
}