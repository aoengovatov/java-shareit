package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestOutDto create(ItemRequestCreateDto dto, long userId);

    List<ItemRequestOutDto> getAllByRequestor(long userId);

    List<ItemRequestOutDto> getAll(long userId, int from, int size);

    ItemRequestOutDto getAllByRequestId(long userId, long requestId);
}
