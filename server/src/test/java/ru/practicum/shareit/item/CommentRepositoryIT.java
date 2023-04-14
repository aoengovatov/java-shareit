package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CommentRepositoryIT {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    public void addUserAndItemAndComment() {
        long itemId = 1L;
        User user = new User(1L, "User 1 name", "enail1@email.ru");
        userRepository.save(user);
        Item item = new Item(itemId, "Item Name", "Item Description",
                true, user, null);
        itemRepository.save(item);
        Comment comment = new Comment("Comment text", itemId, "Author Name",
                LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Test
    void getCommentByItemId_whenInvoked_thenReturnCollectionComments() {
        long itemId = 1L;

        List<Comment> response = commentRepository.getCommentByItemId(itemId);

        assertEquals(response.size(), 1);
    }
}