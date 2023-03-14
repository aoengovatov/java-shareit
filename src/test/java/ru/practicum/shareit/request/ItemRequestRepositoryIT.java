package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryIT {

    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void addItemRequests() {
        User requestor1 = new User(1L, "User 1 name", "enail1@email.ru");
        userRepository.save(requestor1);
        ItemRequest itemRequest1 = new ItemRequest("Request 1", requestor1, LocalDateTime.now());
        itemRequestRepository.save(itemRequest1);
        User requestor2 = new User(2L, "User 2 name", "enail2@email.ru");
        userRepository.save(requestor2);
        ItemRequest itemRequest2 = new ItemRequest("Request 2", requestor2, LocalDateTime.now());
        itemRequestRepository.save(itemRequest2);
    }

    @Test
    void getAllByRequestor() {
        long userId = 1L;
        List<ItemRequest> response = itemRequestRepository.getAllByRequestor(userId);

        assertEquals(response.size(), 1);
    }

    @Test
    void getAllRequestWithoutUser() {
        long userId = 1L;
        List<ItemRequest> response = itemRequestRepository.getAllByRequestor(userId);

        assertEquals(response.size(), 1);
    }

    @AfterEach
    private void deleteItemRequests() {
        itemRequestRepository.deleteAll();
    }
}