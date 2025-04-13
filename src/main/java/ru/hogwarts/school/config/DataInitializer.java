package ru.hogwarts.school.config;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

@Component
public class DataInitializer {

    private final StudentService studentService;
    private final FacultyService facultyService;

    public DataInitializer(StudentService studentService, FacultyService facultyService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
        //   initTestData();
    }

    private void initTestData() {
        Faculty gryffindor = new Faculty(1L, "Гриффиндор", "красный");
        Faculty slytherin = new Faculty(2L, "Слизерин", "зелёный");

        facultyService.addFaculty(gryffindor);
        facultyService.addFaculty(slytherin);

        Student harry = new Student(1L, "Гарри Поттер", 17);
        Student hermione = new Student(2L, "Гермиона Грейнджер", 17);
        Student draco = new Student(3L, "Драко Малфой", 16);

        studentService.addStudent(harry);
        studentService.addStudent(hermione);
        studentService.addStudent(draco);
    }
}