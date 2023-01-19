package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping
    List<User> getAll(){
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    UserDto getById(@PathVariable Long userId){
        return userService.getById(userId);
    }
    @PostMapping
    UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping ("/{userId}")
    UserDto update(@PathVariable Long userId,
                             @RequestBody UserDto userDto) {
        return userService.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    void deleteById(@PathVariable Long userId){
        userService.deleteById(userId);
    }
}
