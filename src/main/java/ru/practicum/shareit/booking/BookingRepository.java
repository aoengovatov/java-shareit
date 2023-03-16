package ru.practicum.shareit.booking;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.common.MyPageRequest;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select booking from Booking booking " +
            "where booking.booker.id = ?1")
    List<Booking> getAllByUser(long userId, MyPageRequest request);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = ?1 " +
            "and booking.item.id = ?2 " +
            "and booking.end < now() ")
    List<Booking> getAllByBookerAndItemId(long bookerId, long itemId);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = ?1 " +
            "and booking.start >= now() " +
            "order by booking.start desc")
    List<Booking> getAllByUserStateFuture(long userId, MyPageRequest myPageRequest);

    @Query("select booking from Booking booking " +
            "where booking.item.id in (?1) " +
            "order by booking.start desc")
    List<Booking> getAllBookingByItemId(List<Long> itemIds, MyPageRequest request);

    @Query("select booking from Booking booking " +
            "where booking.item.id in (?1) " +
            "and booking.start >= now() " +
            "order by booking.start desc")
    List<Booking> getAllBookingByItemSortFuture(List<Long> itemIds, MyPageRequest myPageRequest);

    @Query("select booking from Booking booking " +
            "where booking.item.id in (?1) " +
            "and booking.status = ?2 " +
            "order by booking.start desc")
    List<Booking> getAllBookingByItemSortStatus(List<Long> itemIds, BookingStatus state, MyPageRequest myPageRequest);

    @Query("select booking from Booking booking " +
            "where booking.item.id in (?1) " +
            "and booking.start < now() " +
            "and booking.end > now() " +
            "order by booking.start desc")
    List<Booking> getAllBookingByItemSortCurrent(List<Long> itemIds, MyPageRequest myPageRequest);

    @Query("select booking from Booking booking " +
            "where booking.item.id in (?1) " +
            "and booking.end < now() " +
            "order by booking.start desc")
    List<Booking> getAllBookingByItemSortPast(List<Long> itemIds, MyPageRequest myPageRequest);

    @Query("select booking from Booking booking " +
            "where booking.item.id in (?1) " +
            "and booking.status = ?2 ")
    List<Booking> getAllBookingByItem(List<Long> itemIds, BookingStatus status, Sort sort);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = ?1 " +
            "and booking.status = ?2 " +
            "order by booking.start desc")
    List<Booking> getAllByUserState(long userId, BookingStatus state, MyPageRequest myPageRequest);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = ?1 " +
            "and booking.start < now() " +
            "and booking.end > now() " +
            "order by booking.start desc")
    List<Booking> getAllByUserStateCurrent(long userId, MyPageRequest myPageRequest);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = ?1 " +
            "and booking.end < now() " +
            "order by booking.start desc")
    List<Booking> getAllByUserStatePast(long userId, MyPageRequest myPageRequest);
}