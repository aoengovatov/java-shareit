package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.model.Comment;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(CommentCreateDto dto) {
        return new Comment(dto.getId(), dto.getText(), dto.getItemId(),
                dto.getAuthorName(), dto.getCreated());
    }

    public static CommentOutDto toCommentOutDto(Comment comment) {
        return new CommentOutDto(comment.getId(), comment.getText(),
                comment.getAuthorName(), comment.getCreated());
    }
}
