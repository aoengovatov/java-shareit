package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestCreateDtoTest {

    @Autowired
    private JacksonTester<ItemRequestCreateDto> json;

    @Test
    void testItemRequestCreateDto() throws Exception {
        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto(
                "description");

        JsonContent<ItemRequestCreateDto> result = json.write(itemRequestCreateDto);

        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("description");
    }
}