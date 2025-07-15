package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;


    @Autowired
    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty findFaculty (long id) {
        logger.info("Was invoked method to find faculty with id={}", id);
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method to edit faculty with id={}", faculty.getId());
        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        } logger.warn("Attempted to edit non-existent faculty with id={}", faculty.getId());
        return null;
    }

    public Faculty deleteFaculty(long id) {
        logger.info("Was invoked method to delete faculty with id={}", id);
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty != null) {
            facultyRepository.deleteById(id);
        } else {
            logger.warn("Attempted to delete non-existent faculty with id={}", id);
        }
        return faculty;
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        logger.info("Was invoked method to get faculties by color={}", color);
        return facultyRepository.findAll().stream()
                    .filter(faculty -> faculty.getColor().equals(color))
                    .collect(Collectors.toList());
        }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method to add faculty with name={}", faculty.getName());
        return facultyRepository.save(faculty);
    }

    public Collection<Faculty> searchFaculties(String query) {
        logger.debug("Was invoked method to search faculties by query={}", query);
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(query, query);
    }

    public List<Student> getStudentsByFaculty(Long facultyId) {
        logger.info("Was invoked method to get students by facultyId={}", facultyId);
        return studentRepository.findByFacultyId(facultyId);
    }

}
