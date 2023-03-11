package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    ItemRequestRepository itemRequestRepository;

    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    void createItem_whenInvoked_thenReturnedItem() {
        long userId = 0L;
        long itemId = 0L;
        long itemRequestId = 0L;
        User owner = new User();
        User requestor = new User();
        ItemRequest request = new ItemRequest(itemRequestId, "description",
                requestor, LocalDateTime.now());
        Item expectedItem = new Item(itemId, "Item name",
                "Item description", true, owner, request);
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setRequestId(expectedItem.getRequest().getId());
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(itemRequestId)).thenReturn(Optional.of(request));
        when(itemRepository.save(any())).thenReturn(expectedItem);

        ItemOutDto actualItem = itemService.create(itemCreateDto, userId);

        assertEquals(expectedItem.getId(), actualItem.getId());
        assertEquals(expectedItem.getName(), actualItem.getName());
        assertEquals(expectedItem.getDescription(), actualItem.getDescription());
        assertEquals(expectedItem.getAvailable(), actualItem.getAvailable());
        assertEquals(expectedItem.getRequest().getId(), actualItem.getRequestId());
    }

    @Test
    void update() {
    }

    @Test
    void getAllItems_whenInvoked_thenReturnedCollectionItems() {
        Item expectedItem = new Item();
        when(itemRepository.findAll()).thenReturn(List.of(expectedItem));

        List<ItemOutDto> actualItems = itemService.getAll();

        assertEquals(actualItems.size(), 1);
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void getItemById_whenItemFound_thenReturnedItem() {
        long itemId = 0L;
        long userId = 0L;
        Item expectedItem = new Item(itemId, "Item name", "Item description", true,
                new User(userId, "User Name", "email"), null);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));

        ItemOutDto actualItem = itemService.getById(itemId, userId);

        assertEquals(expectedItem.getId(), actualItem.getId());
    }

    @Test
    void getAllByUser_whenItemFound_thenReturnedCollectionItems() {
        long userId = 0L;
        Item expectedItem = new Item();
        when(itemRepository.getAllByUser(userId)).thenReturn(List.of(expectedItem));

        List<ItemOutDto> actualItems = itemService.getAllByUser(userId);

        assertEquals(actualItems.size(), 1);
        verify(itemRepository, times(1)).getAllByUser(userId);

    }

    @Test
    void getSearch_whenTextNotBlank_thenReturnedCollectionItems() {
        String text = "some text";
        Item expectedItem = new Item();
        when(itemRepository.getSearch(text)).thenReturn(List.of(expectedItem));

        List<ItemOutDto> actualItems = itemService.getSearch(text);

        assertEquals(actualItems.size(), 1);
        verify(itemRepository, times(1)).getSearch(text);
    }

    @Test
    void getSearch_whenTextIsBlank_thenReturnedEmptyCollection() {
        String text = "";

        List<ItemOutDto> actualItems = itemService.getSearch(text);

        assertEquals(actualItems.size(), 0);
        verify(itemRepository, never()).getSearch(text);
    }

    @Test
    void addComment() {
    }
}