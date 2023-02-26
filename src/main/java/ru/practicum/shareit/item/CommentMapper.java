package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public static Comment toCommentFromCreateDto(CommentCreateDto dto, long itemId,
                                                 String authorName, LocalDateTime created) {
        return new Comment(dto.getText(), itemId, authorName, created);
    }

    public static CommentOutDto toCommentOutDto(Comment comment) {
        return new CommentOutDto(comment.getId(), comment.getText(),
                comment.getAuthorName(), comment.getCreated());
    }
}
