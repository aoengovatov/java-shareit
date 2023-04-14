package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerIT {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemClient itemClient;

    @SneakyThrows
    @Test
    void createItem_whenItemNameIsNotValid_returnStatusBadRequest() {
        long userId = 0L;
        ItemDto itemDto = new ItemDto(0L, null, "Item description",
                true, null);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.create(itemDto, userId)).thenReturn(response);

        mockMvc.perform(post("/items")
                        .header(SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(itemClient, times(0)).create(itemDto, userId);
    }

    @SneakyThrows
    @Test
    void createItem_whenItemDescriptionIsNotValid_returnStatusBadRequest() {
        long userId = 0L;
        ItemDto itemDto = new ItemDto(0L, "Item name", null,
                true, null);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.create(itemDto, userId)).thenReturn(response);

        mockMvc.perform(post("/items")
                        .header(SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(itemClient, times(0)).create(itemDto, userId);
    }

    @SneakyThrows
    @Test
    void createItem_whenItemAvailableIsNotValid_returnStatusBadRequest() {
        long userId = 0L;
        ItemDto itemDto = new ItemDto(0L, "Item name", "Item description",
                null, null);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.create(itemDto, userId)).thenReturn(response);

        mockMvc.perform(post("/items")
                        .header(SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(itemClient, times(0)).create(itemDto, userId);
    }
}