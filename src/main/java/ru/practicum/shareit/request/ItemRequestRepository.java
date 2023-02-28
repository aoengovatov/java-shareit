package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.common.MyPageRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("select request from  ItemRequest request " +
            "where request.requestor.id = ?1 " +
            "order by request.created desc")
    List<ItemRequest> getAllByRequestor(long userId);

    @Query("select request from  ItemRequest request " +
            "where request.requestor.id <> ?1 " +
            "order by request.created desc")
    List<ItemRequest> getAllRequestWithoutUser(long userId, MyPageRequest myPageRequest);
}
