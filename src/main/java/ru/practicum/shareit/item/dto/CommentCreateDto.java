package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {

    @NotBlank(groups = {Create.class, Update.class})
    private String text;
}