package ru.practicum.shareit.item.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentOutDto {

    private long id;

    private String text;

    private String authorName;

    private LocalDateTime created;
}
