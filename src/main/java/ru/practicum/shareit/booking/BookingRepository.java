package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select booking from Booking booking " +
            "where booking.booker.id = ?1 " +
            "order by booking.start desc")
    List<Booking> getAllByUser(long userId);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = ?1 " +
            "and booking.start >= now() " +
            "order by booking.start desc")
    List<Booking> getAllByUserStateFuture(long userId);


    @Query(value = "select * from booking as b " +
            "left join items as i on i.id = b.item_id " +
            "where i.owner_id = ?1 " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getAllBookingItemByOwner(long userId);

    @Query(value = "select * from booking as b " +
            "left join items as i on i.id = b.item_id " +
            "where i.owner_id = ?1 " +
            "and b.start_date >= now() " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getAllBookingItemByOwnerSortFuture(long userId);

    @Query(value = "select * from booking as b " +
            "left join items as i on i.id = b.item_id " +
            "where i.owner_id = ?1 " +
            "and b.status = 'WAITING' " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getAllBookingItemByOwnerSortWaiting(long userId);

    @Query(value = "select * from booking as b " +
            "left join items as i on i.id = b.item_id " +
            "where i.owner_id = ?1 " +
            "and b.status = 'REJECTED' " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getAllBookingItemByOwnerSortRejected(long userId);

    @Query("select booking from Booking booking " +
            "where booking.item.id in (?1) ")
    List<Booking> getAllBookingByItem(List<Long> itemIds);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = ?1 " +
            "and booking.status = ?2 " +
            "order by booking.start desc")
    List<Booking> getAllByUserState(long userId, BookingStatus state);
}
