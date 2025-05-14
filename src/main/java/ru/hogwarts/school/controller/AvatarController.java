package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;
import org.springframework.data.repository.PagingAndSortingRepository;



@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarRepository avatarRepository;

    @Autowired
    public AvatarController(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    @GetMapping("/page")
    public Page<Avatar> getAvatarsByPage(@RequestParam int page, @RequestParam int size) {
        return avatarRepository.findAll(PageRequest.of(page, size));
    }

}