package ru.practicum.shareit.request;

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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerIT {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ItemRequestClient itemRequestClient;

    @SneakyThrows
    @Test
    void create_whenItemRequestDescriptionIsEmpty_thenReturnStatusBadRequest() {
        long userId = 0L;
        ItemRequestDto requestDto = new ItemRequestDto("");
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(itemRequestClient.create(requestDto, userId)).thenReturn(response);

        mockMvc.perform(post("/requests")
                        .header(SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(itemRequestClient, never()).create(requestDto, userId);
    }
}