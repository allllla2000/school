package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.hogwarts.school.model.Avatar;

//public interface AvatarRepository extends PagingAndSortingRepository<Avatar, Long> {
//}

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
}