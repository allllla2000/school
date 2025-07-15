package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository <Student, Long> {
    List<Student> findByAgeBetween(int min, int max);
    List<Student> findByFacultyId(Long facultyId);

    @Query("SELECT COUNT(s) FROM Student s")
    int getStudentCount();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageAge();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC LIMIT 5")
    List<Student> findLastFiveStudents();

}
