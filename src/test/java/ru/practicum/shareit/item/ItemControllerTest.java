package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    void getAllItems_whenInvoked_thenResponseStatusOkAndReturnedCollectionItemsInBody() {
        List<ItemOutDto> expectedItems = List.of(new ItemOutDto());
        when(itemService.getAll()).thenReturn(expectedItems);

        ResponseEntity<List<ItemOutDto>> response = itemController.getAll(0L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItems, response.getBody());
        verify(itemService, times(1)).getAll();
    }

    @Test
    void getAllItemsByUserId_whenInvoked_thenResponseStatusOkAndReturnedCollectionItemsInBody() {
        long userId = 1L;
        List<ItemOutDto> expectedItems = List.of(new ItemOutDto());
        when(itemService.getAllByUser(userId)).thenReturn(expectedItems);

        ResponseEntity<List<ItemOutDto>> response = itemController.getAll(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItems, response.getBody());
        verify(itemService, times(1)).getAllByUser(userId);
    }

    @Test
    void getItemById_whenInvoked_thenResponseStatusOkAndReturnedItemInBody() {
        long userId = 0L;
        long itemId = 0L;
        ItemOutDto expectedItem = new ItemOutDto();
        when(itemService.getById(itemId, userId)).thenReturn(expectedItem);

        ResponseEntity<ItemOutDto> response = itemController.getAll(userId, itemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItem, response.getBody());
        verify(itemService, times(1)).getById(itemId, userId);
    }

    @Test
    void createItem_whenInvoked_thenResponseStatusOkAndReturnedItemInBody() {
        long userId = 0L;
        Item expectedItem = new Item(0L, "Item name", "Item description",
                true, new User(), null);
        ItemCreateDto expItemCreate = new ItemCreateDto(expectedItem.getId(), expectedItem.getName(),
                expectedItem.getDescription(), expectedItem.getAvailable(), null);
        when(itemService.create(any(), eq(userId))).thenReturn(ItemMapper.toItemOutDto(expectedItem));

        ResponseEntity<ItemOutDto> response = itemController.create(userId, expItemCreate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItem.getId(), Objects.requireNonNull(response.getBody()).getId());
        assertEquals(expectedItem.getName(), response.getBody().getName());
        assertEquals(expectedItem.getDescription(), response.getBody().getDescription());
        assertEquals(expectedItem.getAvailable(), response.getBody().getAvailable());
        verify(itemService, times(1)).create(any(), eq(userId));
    }

    @Test
    void updateItem_whenInvoked_thenResponseStatusOkAndReturnedUpdatedItemInBody() {
        long itemId = 0L;
        long userId = 0L;
        Item expectedItem = new Item(0L, "Item name", "Item description",
                true, new User(), null);
        ItemOutDto itemUpdateOut = new ItemOutDto(expectedItem.getId(), expectedItem.getName(),
               expectedItem.getDescription(), expectedItem.getAvailable(), null);
        when(itemService.update(any(), eq(itemId), eq(userId))).thenReturn(ItemMapper.toItemOutDto(expectedItem));

        ResponseEntity<ItemOutDto> response = itemController.update(userId, itemId, itemUpdateOut);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItem.getId(), Objects.requireNonNull(response.getBody()).getId());
        assertEquals(expectedItem.getName(), response.getBody().getName());
        assertEquals(expectedItem.getDescription(), response.getBody().getDescription());
        assertEquals(expectedItem.getAvailable(), response.getBody().getAvailable());
        verify(itemService, times(1)).update(any(), eq(itemId), eq(userId));
    }

    @Test
    void addComment_whenInvoked_thenResponseStatusOkAndReturnedCommentInBody() {
        long userId = 0L;
        long itemId = 0L;
        CommentOutDto expectedComment = new CommentOutDto();
        when(itemService.addComment(any(), eq(userId), eq(itemId))).thenReturn(expectedComment);

        ResponseEntity<CommentOutDto> response = itemController.addComment(userId, itemId, new CommentCreateDto());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedComment, response.getBody());
        verify(itemService, times(1)).addComment(any(), eq(userId), eq(itemId));
    }

    @Test
    void searchItem_whenInvoked_thenResponseStatusOkAndReturnedItemInBody() {
        String text = "some text";
        List<ItemOutDto> expectedItems = List.of(new ItemOutDto());
        when(itemService.getSearch(text)).thenReturn(expectedItems);

        ResponseEntity<List<ItemOutDto>> response = itemController.search(text);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItems, response.getBody());
        verify(itemService, times(1)).getSearch(text);
    }
}