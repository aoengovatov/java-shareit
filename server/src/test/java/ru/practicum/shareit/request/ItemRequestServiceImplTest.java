package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.common.MyPageRequest;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    ItemRequestRepository itemRequestRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;


    @Test
    void create_whenUserNotFound_thenReturnUserNotFoundException() {
        long userId = 0L;

        assertThrows(UserNotFoundException.class, () ->
                itemRequestService.create(new ItemRequestCreateDto(), userId));
    }

    @Test
    void create_whenInvoked_thenReturnItemRequest() {
        long userId = 0L;
        User requestor = new User(userId, "User name", "email@email.ru");
        ItemRequest itemRequest = new ItemRequest(0L,"Request Description", requestor,
                LocalDateTime.now());
        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto("Request Description");
        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        ItemRequestOutDto actualIItem = itemRequestService.create(itemRequestCreateDto, userId);

        assertEquals(itemRequest.getDescription(), actualIItem.getDescription());
    }

    @Test
    void getAllByRequestor_whenUserNotFound_thenReturnUserNotFoundException() {
        long userId = 0L;
        long requestId = 0L;

        assertThrows(UserNotFoundException.class, () ->
                itemRequestService.getAllByRequestId(userId, requestId));
    }

    @Test
    void getAllByRequestor_whenInvoked_thenReturnCollectionItemRequests() {
        long userId = 0L;
        long requestId = 0L;
        User requestor = new User(userId, "User name", "email@email.ru");
        ItemRequest itemRequest = new ItemRequest(requestId,"Request Description", requestor,
                LocalDateTime.now());
        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.getAllByRequestor(userId)).thenReturn(List.of(itemRequest));

        List<ItemRequestOutDto> actualIItem = itemRequestService.getAllByRequestor(userId);

        assertEquals(actualIItem.size(), 1);
    }

    @Test
    void getAllByRequestId_whenUserNotFound_thenReturnUserNotFoundException() {
        long userId = 0L;

        assertThrows(UserNotFoundException.class, () ->
                itemRequestService.getAllByRequestor(userId));
    }

    @Test
    void getAllByRequestId_whenItemRequestNotFound_thenReturnItemRequestNotFoundException() {
        long userId = 0L;
        long requestId = 0L;
        User requestor = new User(userId, "User name", "email@email.ru");
        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));

        assertThrows(ItemRequestNotFoundException.class, () ->
                itemRequestService.getAllByRequestId(userId, requestId));
    }

    @Test
    void getAllByRequestId_whenInvoked_thenReturnItemRequest() {
        long userId = 0L;
        long requestId = 0L;
        User requestor = new User(userId, "User name", "email@email.ru");
        ItemRequest itemRequest = new ItemRequest(requestId,"Request Description", requestor,
                LocalDateTime.now());
        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));

        ItemRequestOutDto actualIItem = itemRequestService.getAllByRequestId(userId, requestId);

        assertEquals(itemRequest.getId(), actualIItem.getId());
        assertEquals(itemRequest.getDescription(), actualIItem.getDescription());
        assertEquals(itemRequest.getCreated(), actualIItem.getCreated());
    }

    @Test
    void getAll_whenInvoked_thenReturnCollectionItemRequests() {
        long userId = 0L;
        long requestId = 0L;
        User requestor = new User(userId, "User name", "email@email.ru");
        ItemRequest itemRequest = new ItemRequest(requestId,"Request Description", requestor,
                LocalDateTime.now());
        when(itemRequestRepository.getAllRequestWithoutUser(userId, new MyPageRequest(0, 10,
                Sort.unsorted()))).thenReturn(List.of(itemRequest));

        List<ItemRequestOutDto> actualIItem = itemRequestService.getAll(userId, 0, 10);

        assertEquals(actualIItem.size(), 1);
    }
}