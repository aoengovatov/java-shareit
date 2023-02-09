package ru.practicum.shareit.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private long id;

    private String name;

    private String email;
}