package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.Create;
import ru.practicum.shareit.booking.Update;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingCreateDto {

    private long id;

    @FutureOrPresent(groups = {Create.class, Update.class})
    private LocalDateTime start;

    @Future(groups = {Create.class, Update.class})
    private LocalDateTime end;

    @NotBlank(groups = {Update.class})
    private BookingStatus status;

    @NotNull(groups = {Create.class, Update.class})
    private long itemId;
}
