package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void create_whenInvoked_thenReturnStatusOk() {
        long userId = 0L;
        ItemRequestCreateDto requestDto = new ItemRequestCreateDto("Request Description");
        when(itemRequestService.create(any(), eq(userId))).thenReturn(new ItemRequestOutDto());

        mockMvc.perform(post("/requests")
                        .header(SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).create(any(), eq(userId));
    }

    @SneakyThrows
    @Test
    void getAllByRequestor_whenInvoked_thenReturnStatusOk() {
        long requestorId = 0L;
        when(itemRequestService.getAllByRequestor(requestorId)).thenReturn(List.of(new ItemRequestOutDto()));

        mockMvc.perform(get("/requests")
                        .header(SHARER_USER_ID, requestorId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).getAllByRequestor(requestorId);
    }

    @SneakyThrows
    @Test
    void getById_whenInvoked_thenReturnStatusOk() {
        long userId = 0L;
        long requestId = 0L;
        when(itemRequestService.getAllByRequestId(userId, requestId)).thenReturn(new ItemRequestOutDto());

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header(SHARER_USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).getAllByRequestId(userId, requestId);
    }

    @SneakyThrows
    @Test
    void getAll_whenInvoked_thenReturnStatusOk() {
        long userId = 0L;
        long requestId = 0L;
        when(itemRequestService.getAll(userId, 0, 10)).thenReturn(List.of(new ItemRequestOutDto()));

        mockMvc.perform(get("/requests/all", requestId)
                        .header(SHARER_USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).getAll(userId, 0, 10);
    }
}