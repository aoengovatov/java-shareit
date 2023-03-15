package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.common.MyPageRequest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryIT {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Autowired
    BookingRepository bookingRepository;

    @BeforeEach
    public void addUsersAndItemsAndBookings() {
        User user1 = new User(1L, "User 1 name", "enail1@email.ru");
        userRepository.save(user1);
        Item item1 = new Item(1L, "ItemName1", "ItemDescription1",
                true, user1, null);
        itemRepository.save(item1);
        User booker1 = new User(2L, "User name booker", "enail2@email.ru");
        userRepository.save(booker1);
        Booking booking1 = new Booking(1L, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), item1, booker1, BookingStatus.APPROVED);
        bookingRepository.save(booking1);
        Booking booking2 = new Booking(2L, LocalDateTime.now(),
                LocalDateTime.now().plusDays(1), item1, booker1, BookingStatus.APPROVED);
        bookingRepository.save(booking2);
        Booking booking3 = new Booking(3L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item1, booker1, BookingStatus.APPROVED);
        bookingRepository.save(booking3);
    }

    @Test
    void getAllByUser_whenInvoked_thenReturnCollectionItems() {
        long userId = 2L;
        MyPageRequest myPage = new MyPageRequest(0, 10,
                Sort.by(Sort.Direction.DESC, "start"));
        List<Booking> response = bookingRepository.getAllByUser(userId, myPage);

        assertEquals(response.size(), 3);
    }

    @Test
    void getAllByBookerAndItemId_whenInvoked_thenReturnCollectionItems() {
        long userId = 2L;
        long itemId = 1L;
        List<Booking> response = bookingRepository.getAllByBookerAndItemId(userId, itemId);

        assertEquals(response.size(), 1);
    }

    @Test
    void getAllByUserStateFuture_whenInvoked_thenReturnCollectionItems() {
        long userId = 2L;
        List<Booking> response = bookingRepository.getAllByUserStateFuture(userId);

        assertEquals(response.size(), 1);
    }

    @Test
    void getAllBookingByItemId_whenInvoked_thenReturnCollectionItems() {
        long itemId = 1L;
        MyPageRequest myPage = new MyPageRequest(0, 10,
                Sort.by(Sort.Direction.DESC, "start"));
        List<Booking> response = bookingRepository.getAllBookingByItemId(List.of(itemId), myPage);

        assertEquals(response.size(), 3);
    }

    @Test
    void getAllBookingByItemSortFuture_whenInvoked_thenReturnCollectionItems() {
        long itemId = 1L;

        List<Booking> response = bookingRepository.getAllBookingByItemSortFuture(List.of(itemId));

        assertEquals(response.size(), 1);
    }

    @Test
    void getAllBookingByItemSortStatus_whenInvoked_thenReturnCollectionItems() {
        long itemId = 1L;
        BookingStatus status = BookingStatus.APPROVED;

        List<Booking> response = bookingRepository.getAllBookingByItemSortStatus(List.of(itemId), status);

        assertEquals(response.size(), 3);
    }

    @Test
    void getAllBookingByItemSortCurrent_whenInvoked_thenReturnCollectionItems() {
        long itemId = 1L;

        List<Booking> response = bookingRepository.getAllBookingByItemSortCurrent(List.of(itemId));

        assertEquals(response.size(), 1);
    }

    @Test
    void getAllBookingByItemSortPast_whenInvoked_thenReturnCollectionItems() {
        long itemId = 1L;

        List<Booking> response = bookingRepository.getAllBookingByItemSortPast(List.of(itemId));

        assertEquals(response.size(), 1);
    }

    @Test
    void getAllBookingByItem_whenInvoked_thenReturnCollectionItems() {
        long itemId = 1L;
        BookingStatus status = BookingStatus.APPROVED;
        List<Booking> response = bookingRepository.getAllBookingByItem(List.of(itemId), status,
                Sort.by(Sort.Direction.DESC, "start"));

        assertEquals(response.size(), 3);
    }

    @Test
    void getAllByUserState_whenInvoked_thenReturnCollectionItems() {
        long userId = 2L;
        BookingStatus status = BookingStatus.APPROVED;
        List<Booking> response = bookingRepository.getAllByUserState(userId, status);

        assertEquals(response.size(), 3);
    }

    @Test
    void getAllByUserStateCurrent_whenInvoked_thenReturnCollectionItems() {
        long userId = 2L;

        List<Booking> response = bookingRepository.getAllByUserStateCurrent(userId);

        assertEquals(response.size(), 1);
    }

    @Test
    void getAllByUserStatePast_whenInvoked_thenReturnCollectionItems() {
        long userId = 2L;

        List<Booking> response = bookingRepository.getAllByUserStatePast(userId);

        assertEquals(response.size(), 1);
    }
}