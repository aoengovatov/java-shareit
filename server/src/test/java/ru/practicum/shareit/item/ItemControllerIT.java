package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemOutDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private ItemService itemService;

    @SneakyThrows
    @Test
    void getAll_whenUserIdNotPositiveOrNull_thenReturnListItemInBody() {
       long userId = 0L;

        mockMvc.perform(get("/items", userId)
                        .header(SHARER_USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService, times(1)).getAll();
        verify(itemService, never()).getAllByUser(userId);
    }

    @SneakyThrows
    @Test
    void getAll_whenUserIdIsPositive_thenReturnListItemInBody() {
        long userId = 1L;

        mockMvc.perform(get("/items", userId)
                        .header(SHARER_USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService, never()).getAll();
        verify(itemService, times(1)).getAllByUser(userId);
    }

    @SneakyThrows
    @Test
    void getAll_whenItemIdIsGiven_thenReturnStatusOk() {
        long userId = 0L;
        long itemId = 0L;

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header(SHARER_USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService, times(1)).getById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void addComment() {
        long userId = 0L;
        long itemId = 0L;
        CommentCreateDto commentCreateDto = new CommentCreateDto("New comment");
        CommentOutDto expectedComment = new CommentOutDto(0L, "Text comment", "Author Name",
                LocalDateTime.now());
        when(itemService.addComment(any(), eq(userId), eq(itemId))).thenReturn(expectedComment);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentCreateDto)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemService, times(1)).addComment(any(), eq(userId), eq(itemId));
    }

    @SneakyThrows
    @Test
    void update_whenInvoked_thenReturnStatusOk() {
        long userId = 0L;
        long itemId = 0L;
        ItemCreateDto itemCreateDto = new ItemCreateDto(itemId, "Item name", "Item description",
                true, null);
        ItemOutDto itemOutDto = new ItemOutDto(itemId, "Item name", "Item description",
                true, null);
        when(itemService.update(any(), eq(userId), eq(itemId))).thenReturn(itemOutDto);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header(SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemCreateDto)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemService, times(1)).update(any(), eq(userId), eq(itemId));
    }

    @SneakyThrows
    @Test
    void search_whenInvoked_thenReturnStatusOk() {
        long userId = 0L;
        long itemId = 0L;
        String text = "some text search";
        ItemOutDto itemOutDto = new ItemOutDto(itemId, "Item name", "Item description",
                true, null);
        when(itemService.getSearch(text)).thenReturn(List.of(itemOutDto));

        mockMvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("text", text))
                .andExpect(status().isOk())
                .andDo(print());

        verify(itemService, times(1)).getSearch(text);
    }
}