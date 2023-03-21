package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestOutDtoTest {

    @Autowired
    private JacksonTester<ItemRequestOutDto> json;

    @Test
    void testItemRequestOutDto() throws Exception {
        List<ItemAnswerOutDto> itemAnswer = List.of(new ItemAnswerOutDto(1L, "Name", "description",
                true, 2L));
        ItemRequestOutDto itemRequestOutDto = new ItemRequestOutDto(
                1L,
                "description",
                LocalDateTime.now(),
                itemAnswer);

        JsonContent<ItemRequestOutDto> result = json.write(itemRequestOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("Name");
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].requestId").isEqualTo(2);
    }
}