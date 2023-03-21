package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.MyPageRequest;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemAnswerOutDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public ItemRequestOutDto create(ItemRequestCreateDto dto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Не найден User с id: " + userId));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(dto, user, LocalDateTime.now());
        log.info("Создан новый ItemRequest c id: {} User с id: {}", itemRequest.getId(), userId);
        return ItemRequestMapper.toItemRequestOut(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestOutDto> getAllByRequestor(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Не найден User с id: " + userId));
        List<ItemRequest> itemRequest = itemRequestRepository.getAllByRequestor(userId);
        List<ItemRequestOutDto> requestOut = getItemsByRequest(itemRequest);
        log.info("Запрос списка всех ItemRequest User с id: {}", userId);
        return requestOut;
    }

    @Override
    public ItemRequestOutDto getAllByRequestId(long userId, long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Не найден User с id: " + userId));
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException("Не найден ItemRequest с id: " + requestId));
        List<ItemRequest> itemRequest = new ArrayList<>();
        itemRequest.add(request);
        log.info("Запрос ItemRequest с id: {}", requestId);
        return getItemsByRequest(itemRequest).get(0);
    }

    @Override
    public List<ItemRequestOutDto> getAll(long userId, int from, int size) {
        log.info("Запрос списка всех ItemRequest other c пагинацией from: {}, size: {}", from, size);
        List<ItemRequest> request = itemRequestRepository.getAllRequestWithoutUser(userId,
                new MyPageRequest(from, size, Sort.unsorted()));
        return getItemsByRequest(request);
    }

    private List<ItemRequestOutDto> getItemsByRequest(List<ItemRequest> itemRequest) {
        List<Long> ids = itemRequest.stream()
                .map(ItemRequest::getId)
                .collect(toList());
        Map<ItemRequest, List<Item>> requestItems = itemRepository.getAllByRequestId(ids).stream()
                .collect(groupingBy(Item::getRequest, toList()));
        List<ItemRequestOutDto> requestsOut = new ArrayList<>();
        for (ItemRequest request : itemRequest) {
            List<Item> items = requestItems.get(request);
            ItemRequestOutDto requestOutDto = ItemRequestMapper.toItemRequestOut(request);
            requestOutDto.setItems(new ArrayList<>());
            if (items != null) {
                List<ItemAnswerOutDto> itemAnswer = items.stream()
                        .map(ItemRequestMapper::toItemAnswerDto)
                        .collect(toList());
                requestOutDto.setItems(itemAnswer);
            }
            requestsOut.add(requestOutDto);
        }
        return requestsOut;
    }
}