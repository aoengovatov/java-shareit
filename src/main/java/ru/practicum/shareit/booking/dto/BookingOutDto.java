package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingOutDto {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;

    private BookerDto booker;

    private BookingItemDto item;
}
