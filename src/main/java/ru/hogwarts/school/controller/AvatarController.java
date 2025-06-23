package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.hogwarts.school.service.AvatarService;


@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    @Autowired
    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping("/page")
    public Page<Avatar> getAvatarsByPage(@RequestParam int page, @RequestParam int size) {
        return avatarService.getAvatarsByPage(page, size);
    }

}