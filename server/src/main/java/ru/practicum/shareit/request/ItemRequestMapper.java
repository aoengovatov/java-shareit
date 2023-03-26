package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemAnswerOutDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestCreateDto dto, User user, LocalDateTime created) {
        return new ItemRequest(dto.getDescription(), user, created);
    }

    public static ItemRequestOutDto toItemRequestOut(ItemRequest itemRequest) {
        return new ItemRequestOutDto(itemRequest.getId(), itemRequest.getDescription(),
                itemRequest.getCreated(), new ArrayList<>());
    }


    public static ItemAnswerOutDto toItemAnswerDto(Item item) {
        return new ItemAnswerOutDto(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequest().getId());
    }
}
