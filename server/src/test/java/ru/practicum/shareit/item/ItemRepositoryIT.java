package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryIT {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    @BeforeEach
    public void addUsersAndItemsAndItemRequests() {
        User user1 = new User(1L, "User 1 name", "enail1@email.ru");
        userRepository.save(user1);
        Item item1 = new Item(1L, "ItemName1", "ItemDescription1",
                true, user1, null);
        itemRepository.save(item1);
        User user2 = new User(2L, "User 2 name", "enail2@email.ru");
        userRepository.save(user2);
        ItemRequest itemRequest2 = new ItemRequest("Request 2", user1, LocalDateTime.now());
        itemRequestRepository.save(itemRequest2);
        Item item2 = new Item(2L, "ItemName2", "ItemDescription2",
                true, user2, itemRequest2);
        itemRepository.save(item2);
    }

    @Test
    void getAllByUser_whenInvoked_thenReturnCollectionItems() {
        long userId = 1L;
        List<Item> response = itemRepository.getAllByUser(userId);

        assertEquals(response.size(), 1);
    }

    @Test
    void getSearch_whenInvoked_thenReturnCollectionItems() {
        String searchText = "EMName2";
        List<Item> response = itemRepository.getSearch(searchText);

        assertEquals(response.size(), 1);
    }

    @Test
    void getAllByRequestId_whenInvoked_thenReturnCollectionItems() {
        long requestId = 1L;
        List<Item> response = itemRepository.getAllByRequestId(List.of(requestId));

        assertEquals(response.size(), 1);
    }
}