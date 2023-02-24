package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select item from Item item " +
            "where item.owner.id = ?1 " +
            "order by item.id asc")
    List<Item> getAllByUser(long userId);

    @Query("select item from Item item " +
            "where lower(item.name) like concat('%', ?1, '%') " +
            "or lower(item.description) like concat('%', ?1, '%') " +
            "and item.available = true " +
            "order by item.id asc")
    List<Item> getSearch(String text);
}