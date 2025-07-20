package ru.hogwarts.school.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

@Configuration
public class AvatarDataLoader {

    @Bean
    public CommandLineRunner loadAvatars(AvatarRepository avatarRepository) {
        return args -> {
            avatarRepository.save(new Avatar("avatars/harry.png"));
            avatarRepository.save(new Avatar("avatars/hermione.png"));
            avatarRepository.save(new Avatar("avatars/ron.png"));
        };
    }
}