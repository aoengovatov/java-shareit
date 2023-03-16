package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestOutDto> create(@RequestHeader(SHARER_USER_ID) long userId,
                                    @Validated({Create.class}) @RequestBody ItemRequestCreateDto dto) {
        return ResponseEntity.ok(itemRequestService.create(dto, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestOutDto>> getAllByRequestor(@RequestHeader(SHARER_USER_ID) long userId) {
        return ResponseEntity.ok(itemRequestService.getAllByRequestor(userId));
    }

    @GetMapping("{requestId}")
    public ResponseEntity<ItemRequestOutDto> getById(@RequestHeader(SHARER_USER_ID) long userId,
                                           @PathVariable long requestId) {
        return ResponseEntity.ok(itemRequestService.getAllByRequestId(userId, requestId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestOutDto>> getAll(@RequestHeader(SHARER_USER_ID) long userId,
            @PositiveOrZero @RequestParam (name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam (name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(itemRequestService.getAll(userId, from, size));
    }
}
