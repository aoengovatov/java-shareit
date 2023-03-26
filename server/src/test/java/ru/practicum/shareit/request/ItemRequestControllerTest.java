package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    ItemRequestController itemRequestController;

    @Test
    void createItemRequest_whenInvoked_thenResponseStatusOkAndReturnedItemRequestInBody() {
        long userId = 0L;
        ItemRequestOutDto expectedItemRequest = new ItemRequestOutDto();
        when(itemRequestService.create(any(), eq(userId))).thenReturn(expectedItemRequest);

        ResponseEntity<ItemRequestOutDto> response = itemRequestController.create(userId,
                new ItemRequestCreateDto());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemRequest, response.getBody());
    }

    @Test
    void getAllByRequestor_whenInvoked_thenResponseStatusOkAndReturnedCollectionItemRequestInBody() {
        long userId = 0L;
        List<ItemRequestOutDto> expectedItemRequest = List.of(new ItemRequestOutDto());
        when(itemRequestService.getAllByRequestor(userId)).thenReturn(expectedItemRequest);

        ResponseEntity<List<ItemRequestOutDto>> response = itemRequestController.getAllByRequestor(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemRequest, response.getBody());
    }

    @Test
    void getById_whenInvoked_thenResponseStatusOkAndReturnedItemRequestInBody() {
        long itemRequestId = 0L;
        long userId = 0L;
        ItemRequestOutDto expectedItemRequest = new ItemRequestOutDto();
        when(itemRequestService.getAllByRequestId(userId, itemRequestId)).thenReturn(expectedItemRequest);

        ResponseEntity<ItemRequestOutDto> response = itemRequestController.getById(userId, itemRequestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemRequest, response.getBody());
    }

    @Test
    void getAll_whenInvoked_thenResponseStatusOkAndReturnedCollectionItemRequestInBody() {
        long userId = 0L;
        List<ItemRequestOutDto> expectedItemRequest = List.of(new ItemRequestOutDto());
        when(itemRequestService.getAll(userId, 0, 10)).thenReturn(expectedItemRequest);

        ResponseEntity<List<ItemRequestOutDto>> response = itemRequestController.getAll(userId, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemRequest, response.getBody());
    }
}