package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.common.MyPageRequest;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
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
class BookingServiceImplTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    BookingServiceImpl bookingService;

    @Test
    void create_whenUserNotFound_thenReturnUserNotFoundException() {
        long userId = 0L;

        assertThrows(UserNotFoundException.class, () ->
                bookingService.create(new BookingCreateDto(), userId));
    }

    @Test
    void create_whenItemNotFound_thenReturnItemNotFoundException() {
        long userId = 0L;
        User user = new User(userId, "User name", "email@email.ru");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(ItemNotFoundException.class, () ->
                bookingService.create(new BookingCreateDto(), userId));
    }

    @Test
    void create_whenInvoked_thenReturnItem() {
        long userId = 0L;
        long itemId = 0L;
        long bookerId = 1L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), item, booker, BookingStatus.WAITING);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), itemId);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(expectedBooking);

        BookingOutDto actualBooking = bookingService.create(bookingCreateDto, bookerId);

        assertEquals(expectedBooking.getId(), actualBooking.getId());
        assertEquals(expectedBooking.getStart(), actualBooking.getStart());
        assertEquals(expectedBooking.getEnd(), actualBooking.getEnd());
        assertEquals(expectedBooking.getItem().getId(), actualBooking.getItem().getId());
        assertEquals(expectedBooking.getStatus(), actualBooking.getStatus());
        assertEquals(expectedBooking.getBooker().getId(), actualBooking.getBooker().getId());
    }

    @Test
    void create_whenBookingDataError_thenReturnBadParameterException() {
        long userId = 0L;
        long itemId = 0L;
        long bookerId = 1L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now(), itemId);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(BadParameterException.class, () ->
                bookingService.create(bookingCreateDto, bookerId));
    }

    @Test
    void create_whenBookerMathedWithItemUser_thenReturnIncorrectParameterException() {
        long userId = 1L;
        long itemId = 0L;
        long bookerId = 1L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), itemId);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(IncorrectParameterException.class, () ->
                bookingService.create(bookingCreateDto, bookerId));
    }

    @Test
    void create_whenItemIsNotAvailableForBooking_thenReturnBadParameterException() {
        long userId = 0L;
        long itemId = 0L;
        long bookerId = 1L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                false, user, null);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), itemId);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(BadParameterException.class, () ->
                bookingService.create(bookingCreateDto, bookerId));
    }

    @Test
    void getById_whenBookingNotFound_thenReturnBookingNotFoundException() {
        long userId = 0L;
        long bookingId = 0L;

        assertThrows(BookingNotFoundException.class, () ->
                bookingService.getById(bookingId, userId));
    }

    @Test
    void getById_whenInvoked_thenReturnBooking() {
        long userId = 0L;
        long bookingId = 0L;
        long itemId = 0L;
        long bookerId = 1L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), item, booker, BookingStatus.WAITING);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        BookingOutDto actualBooking = bookingService.getById(bookingId, bookerId);

        assertEquals(expectedBooking.getId(), actualBooking.getId());
    }

    @Test
    void getById_whenBookingCreaterNotMatched_thenReturnBookingNotFoundException() {
        long userId = 0L;
        long bookingId = 0L;
        long itemId = 0L;
        long bookerId = 1L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(2L, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), item, booker, BookingStatus.WAITING);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        assertThrows(BookingNotFoundException.class, () ->
                bookingService.getById(bookingId, bookerId));
    }

    @Test
    void confirm_whenBookingNotFound_thenReturnBookingNotFoundException() {
        long userId = 0L;
        long bookingId = 0L;
        String approved = "true";

        assertThrows(BookingNotFoundException.class, () ->
                bookingService.confirm(bookingId, userId, approved));
    }

    @Test
    void confirm_whenBookingOwnerNotMatchedWithUserId_thenReturnUserNotFoundException() {
        long userId = 0L;
        long bookingId = 0L;
        String approved = "true";
        long itemId = 0L;
        long bookerId = 1L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), item, booker, BookingStatus.WAITING);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        assertThrows(UserNotFoundException.class, () ->
                bookingService.confirm(bookingId, 2L, approved));
    }

    @Test
    void confirm_whenApprovedTrue_thenReturnBooking() {
        long userId = 0L;
        long bookingId = 0L;
        String approved = "true";
        long itemId = 0L;
        long bookerId = 1L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), item, booker, BookingStatus.WAITING);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        BookingOutDto actualBooking = bookingService.confirm(bookingId, userId, approved);

        assertEquals(actualBooking.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    void confirm_whenApprovedFalse_thenReturnBooking() {
        long userId = 0L;
        long bookingId = 0L;
        String approved = "false";
        long itemId = 0L;
        long bookerId = 1L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), item, booker, BookingStatus.WAITING);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        BookingOutDto actualBooking = bookingService.confirm(bookingId, userId, approved);

        assertEquals(actualBooking.getStatus(), BookingStatus.REJECTED);
    }

    @Test
    void confirm_whenApprovedIsBlank_thenReturnBadParameterException() {
        long userId = 0L;
        long bookingId = 0L;
        String approved = "";
        long itemId = 0L;
        long bookerId = 1L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), item, booker, BookingStatus.WAITING);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        assertThrows(BadParameterException.class, () ->
                bookingService.confirm(bookingId, userId, approved));
    }

    @Test
    void getAllByUser_whenUserNotFound_thenReturnUserNotFoundException() {
        long userId = 0L;

        assertThrows(UserNotFoundException.class, () ->
                bookingService.getAllByUser(userId, BookingStatus.ALL, 0, 10));
    }

    @Test
    void getAllByUser_whenBookingStatusIsAll_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now(), item, booker, BookingStatus.APPROVED);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.getAllByUser(userId, new MyPageRequest(0, 10,
                Sort.by(Sort.Direction.DESC, "start")))).thenReturn(List.of(expectedBooking));

        List<BookingOutDto> actualBooking = bookingService.getAllByUser(userId,
                BookingStatus.ALL, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }

    @Test
    void getAllByUser_whenBookingStatusIsFuture_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4), item, booker, BookingStatus.APPROVED);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.getAllByUserStateFuture(userId,
                new MyPageRequest(0, 10, Sort.by(Sort.Direction.DESC, "start"))))
                .thenReturn(List.of(expectedBooking));

        List<BookingOutDto> actualBooking = bookingService.getAllByUser(userId,
                BookingStatus.FUTURE, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }

    @Test
    void getAllByUser_whenBookingStatusIsWaiting_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        BookingStatus state = BookingStatus.WAITING;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4), item, booker, state);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.getAllByUserState(userId, state,
                new MyPageRequest(0, 10, Sort.by(Sort.Direction.DESC, "start"))))
                .thenReturn(List.of(expectedBooking));

        List<BookingOutDto> actualBooking = bookingService.getAllByUser(userId,
                state, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }

    @Test
    void getAllByUser_whenBookingStatusIsRejected_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        BookingStatus state = BookingStatus.REJECTED;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4), item, booker, state);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.getAllByUserState(userId, state,
                new MyPageRequest(0, 10, Sort.by(Sort.Direction.DESC, "start"))))
                .thenReturn(List.of(expectedBooking));

        List<BookingOutDto> actualBooking = bookingService.getAllByUser(userId,
                state, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }

    @Test
    void getAllByUser_whenBookingStatusIsCurrent_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        BookingStatus state = BookingStatus.CURRENT;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4), item, booker, state);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.getAllByUserStateCurrent(userId,
                new MyPageRequest(0, 10, Sort.by(Sort.Direction.DESC, "start"))))
                .thenReturn(List.of(expectedBooking));

        List<BookingOutDto> actualBooking = bookingService.getAllByUser(userId,
                state, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }

    @Test
    void getAllByUser_whenBookingStatusIsPast_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        BookingStatus state = BookingStatus.PAST;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4), item, booker, state);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.getAllByUserStatePast(userId,
                new MyPageRequest(0, 10, Sort.by(Sort.Direction.DESC, "start"))))
                .thenReturn(List.of(expectedBooking));

        List<BookingOutDto> actualBooking = bookingService.getAllByUser(userId,
                state, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }

    @Test
    void getAllBookingItemByOwner_whenUserNotFound_thenReturnUserNotFoundException() {
        long userId = 0L;

        assertThrows(UserNotFoundException.class, () ->
                bookingService.getAllBookingItemByOwner(userId, BookingStatus.ALL, 0, 10));
    }

    @Test
    void getAllBookingItemByOwner_whenBookingStatusIsAll_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        BookingStatus state = BookingStatus.ALL;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4), item, booker, state);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.getAllBookingByItemId(List.of(itemId), new MyPageRequest(0, 10,
                Sort.by(Sort.Direction.DESC, "start")))).thenReturn(List.of(expectedBooking));
        when(itemRepository.getAllByUser(bookerId)).thenReturn(List.of(item));

        List<BookingOutDto> actualBooking = bookingService.getAllBookingItemByOwner(bookerId,
                state, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }

    @Test
    void getAllBookingItemByOwner_whenBookingStatusIsFuture_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        BookingStatus state = BookingStatus.FUTURE;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4), item, booker, state);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.getAllBookingByItemSortFuture(List.of(itemId),
                new MyPageRequest(0, 10, Sort.by(Sort.Direction.DESC, "start"))))
                .thenReturn(List.of(expectedBooking));
        when(itemRepository.getAllByUser(bookerId)).thenReturn(List.of(item));

        List<BookingOutDto> actualBooking = bookingService.getAllBookingItemByOwner(bookerId,
                state, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }

    @Test
    void getAllBookingItemByOwner_whenBookingStatusIsWaiting_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        BookingStatus state = BookingStatus.WAITING;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4), item, booker, state);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.getAllBookingByItemSortStatus(List.of(itemId), state,
                new MyPageRequest(0, 10, Sort.by(Sort.Direction.DESC, "start"))))
                .thenReturn(List.of(expectedBooking));
        when(itemRepository.getAllByUser(bookerId)).thenReturn(List.of(item));

        List<BookingOutDto> actualBooking = bookingService.getAllBookingItemByOwner(bookerId,
                state, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }

    @Test
    void getAllBookingItemByOwner_whenBookingStatusIsRejected_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        BookingStatus state = BookingStatus.REJECTED;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4), item, booker, state);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.getAllBookingByItemSortStatus(List.of(itemId), state,
                new MyPageRequest(0, 10, Sort.by(Sort.Direction.DESC, "start"))))
                .thenReturn(List.of(expectedBooking));
        when(itemRepository.getAllByUser(bookerId)).thenReturn(List.of(item));

        List<BookingOutDto> actualBooking = bookingService.getAllBookingItemByOwner(bookerId,
                state, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }

    @Test
    void getAllBookingItemByOwner_whenBookingStatusIsCurrent_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        BookingStatus state = BookingStatus.CURRENT;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4), item, booker, state);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.getAllBookingByItemSortCurrent(List.of(itemId),
                new MyPageRequest(0, 10, Sort.by(Sort.Direction.DESC, "start"))))
                .thenReturn(List.of(expectedBooking));
        when(itemRepository.getAllByUser(bookerId)).thenReturn(List.of(item));

        List<BookingOutDto> actualBooking = bookingService.getAllBookingItemByOwner(bookerId,
                state, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }

    @Test
    void getAllBookingItemByOwner_whenBookingStatusIsPast_thenReturnCollectionBookings() {
        long userId = 0L;
        long bookerId = 1L;
        long itemId = 0L;
        BookingStatus state = BookingStatus.PAST;
        User user = new User(userId, "User name", "email2@email.ru");
        User booker = new User(bookerId, "booker name", "email@email.ru");
        Item item = new Item(itemId, "Item name", "Item description",
                true, user, null);
        Booking expectedBooking = new Booking(0L, LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4), item, booker, state);
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.getAllBookingByItemSortPast(List.of(itemId),
                new MyPageRequest(0, 10, Sort.by(Sort.Direction.DESC, "start"))))
                .thenReturn(List.of(expectedBooking));
        when(itemRepository.getAllByUser(bookerId)).thenReturn(List.of(item));

        List<BookingOutDto> actualBooking = bookingService.getAllBookingItemByOwner(bookerId,
                state, 0, 10);

        assertEquals(actualBooking.size(), 1);
    }
}