package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.Create;
import ru.practicum.shareit.item.Update;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDto {

    private long id;

    @NotBlank(groups = {Create.class, Update.class})
    private String text;

    private long itemId;

    @NotBlank(groups = {Update.class})
    private String authorName;

    private LocalDateTime created;
}
