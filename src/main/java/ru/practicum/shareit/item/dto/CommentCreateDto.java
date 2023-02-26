package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Create;
import ru.practicum.shareit.item.Update;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {

    private long id;

    @NotBlank(groups = {Create.class, Update.class})
    private String text;

    private long itemId;

    private String authorName;

    private LocalDateTime created;
}