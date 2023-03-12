package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.BadParameterException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void updateItem_whenUserIdNotMatched_thenReturnItemNotFoundException() {
        long itemId = 0L;
        long userId = 0L;
        Item expectedItem = new Item(itemId, "Item name", "Item description", true,
                new User(1L, "User Name", "email"), null);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));

        assertThrows(ItemNotFoundException.class, () -> itemService.update(
                ItemMapper.toItemOutDto(expectedItem), itemId, userId));
    }

    @Test
    void updateItem_whenItemNameIsBlank_thenReturnItemWithOldName() {
        long itemId = 0L;
        long userId = 0L;
        User owner = new User(userId, "User Name", "email");
        Item expectedItem = new Item(itemId, "Item name", "Item description",
                true, owner, null);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        ItemOutDto itemUpdate = ItemMapper.toItemOutDto(expectedItem);
        itemUpdate.setName("");

        ItemOutDto updatedItem = itemService.update(itemUpdate,itemId, userId);

        assertEquals(expectedItem.getName(), updatedItem.getName());
    }

    @Test
    void updateItem_whenItemDescriptionIsBlank_thenReturnItemWithOldDescription() {
        long itemId = 0L;
        long userId = 0L;
        User owner = new User(userId, "User Name", "email");
        Item expectedItem = new Item(itemId, "Item name", "Item description",
                true, owner, null);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        ItemOutDto itemUpdate = ItemMapper.toItemOutDto(expectedItem);
        itemUpdate.setDescription("");

        ItemOutDto updatedItem = itemService.update(itemUpdate,itemId, userId);

        assertEquals(expectedItem.getDescription(), updatedItem.getDescription());
    }

    @Test
    void updateItem_whenItemAvailableIsNull_thenReturnItemWithOldAvailable() {
        long itemId = 0L;
        long userId = 0L;
        User owner = new User(userId, "User Name", "email");
        Item expectedItem = new Item(itemId, "Item name", "Item description",
                true, owner, null);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        ItemOutDto itemUpdate = ItemMapper.toItemOutDto(expectedItem);
        itemUpdate.setAvailable(null);

        ItemOutDto updatedItem = itemService.update(itemUpdate,itemId, userId);

        assertEquals(expectedItem.getAvailable(), updatedItem.getAvailable());
    }

    @Test
    void updateItem_whenItemFieldsIsNull_thenReturnItemWithOldFields() {
        long itemId = 0L;
        long userId = 0L;
        User owner = new User(userId, "User Name", "email");
        Item expectedItem = new Item(itemId, "Item name", "Item description",
                true, owner, null);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        ItemOutDto itemUpdate = ItemMapper.toItemOutDto(expectedItem);
        itemUpdate.setAvailable(null);
        itemUpdate.setName(null);
        itemUpdate.setDescription(null);

        ItemOutDto updatedItem = itemService.update(itemUpdate,itemId, userId);

        assertEquals(expectedItem.getName(), updatedItem.getName());
        assertEquals(expectedItem.getDescription(), updatedItem.getDescription());
        assertEquals(expectedItem.getAvailable(), updatedItem.getAvailable());
    }

    @Test
    void updateItem_whenItemUpdateAllFields_thenReturnItemWithNewFields() {
        long itemId = 0L;
        long userId = 0L;
        User owner = new User(userId, "User Name", "email");
        Item expectedItem = new Item(itemId, "Item name", "Item description",
                true, owner, null);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        ItemOutDto itemUpdate = ItemMapper.toItemOutDto(expectedItem);
        itemUpdate.setAvailable(false);
        itemUpdate.setName("New Name");
        itemUpdate.setDescription("New Description");

        ItemOutDto updatedItem = itemService.update(itemUpdate,itemId, userId);

        assertEquals(updatedItem.getName(), "New Name");
        assertEquals(updatedItem.getDescription(), "New Description");
        assertEquals(updatedItem.getAvailable(), false);
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
        long bookerId = 1L;
        User booker = new User(bookerId, "Booker Name", "email");
        Item expectedItem = new Item(itemId, "Item name", "Item description", true,
                new User(userId, "User Name", "email"), null);
        Booking bookingPast = new Booking(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), expectedItem, booker, BookingStatus.APPROVED);
        Booking bookingNext = new Booking(0L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), expectedItem, booker, BookingStatus.APPROVED);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(bookingRepository.getAllBookingByItem(List.of(itemId), BookingStatus.APPROVED,
                Sort.by(Sort.Direction.DESC, "start"))).thenReturn(List.of(bookingPast, bookingNext));

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
    void addComment_whenInvoked_thenReturnedComment() {
        long itemId = 0L;
        long userId = 0L;
        long bookerId = 1L;
        User owner = new User(userId, "User Name", "email");
        User booker = new User(1L, "Booker Name", "email");
        Item expectedItem = new Item(itemId, "Item name", "Item description",
                true, owner, null);
        Booking booking = new Booking(0L, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                expectedItem, booker, BookingStatus.WAITING);
        Comment expectedComment = new Comment("New comment", itemId, "Booker Name", LocalDateTime.now());
        CommentCreateDto expectedCommentCreateDto = new CommentCreateDto(expectedComment.getText());
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(bookingRepository.getAllByBookerAndItemId(bookerId, itemId)).thenReturn(List.of(booking));
        when(commentRepository.save(any())).thenReturn(expectedComment);

        CommentOutDto actualComment = itemService.addComment(expectedCommentCreateDto, booker.getId(), itemId);

        assertEquals(expectedComment.getId(), actualComment.getId());
        assertEquals(expectedComment.getText(), actualComment.getText());
        assertEquals(booker.getName(), actualComment.getAuthorName());
        assertEquals(expectedComment.getCreated(), actualComment.getCreated());
    }

    @Test
    void addComment_whenBookingsIsEmpty_thenReturnedBadParameterException() {
        long itemId = 0L;
        long userId = 0L;
        long bookerId = 1L;
        User owner = new User(userId, "User Name", "email");
        //User booker = new User(1L, "Booker Name", "email");
        Item expectedItem = new Item(itemId, "Item name", "Item description",
                true, owner, null);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(bookingRepository.getAllByBookerAndItemId(bookerId, itemId)).thenReturn(Collections.emptyList());

        assertThrows(BadParameterException.class, () -> itemService.addComment(
                any(), bookerId, itemId));
    }
}