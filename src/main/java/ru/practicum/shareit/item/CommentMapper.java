package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(CommentDto dto) {
        return new Comment(dto.getId(), dto.getText(), dto.getItemId(),
                dto.getAuthorName(), dto.getCreated());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getItemId(),
                comment.getAuthorName(), comment.getCreated());
    }
}
