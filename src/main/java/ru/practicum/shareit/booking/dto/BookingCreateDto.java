package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateDto {

    private long id;

    @NotNull
    @FutureOrPresent(groups = {Create.class, Update.class})
    private LocalDateTime start;

    @NotNull
    @Future(groups = {Create.class, Update.class})
    private LocalDateTime end;

    @NotNull(groups = {Create.class, Update.class})
    private long itemId;
}
