package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked method to add student: name={}, age={}", student.getName(), student.getAge());
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method to find student with id={}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            logger.error("There is no student with id={}", id);
        }
        return student;
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method to edit student with id={}", student.getId());
        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        }
        logger.warn("Attempted to edit non-existent student with id={}", student.getId());
        return null;
    }

    public Student deleteStudent(long id) {
        logger.info("Was invoked method to delete student with id={}", id);
        if (!studentRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent student with id={}", id);
            return null;
        }
        studentRepository.deleteById(id);
        return null;
    }

    public Collection<Student> getStudentsByAge(int age) {
        logger.debug("Was invoked method to get students by age={}", age);
        return studentRepository.findAll()
                .stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public List<Student> getAllStudents() {
        logger.info("Was invoked method to get all students");
        return studentRepository.findAll();
    }

    public List<Student> getStudentsByAgeBetween(int min, int max) {
        logger.info("Was invoked method to get students between age {} and {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudent(Long id) {
        logger.info("Was invoked method to get faculty by student id={}", id);
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .orElse(null);
    }

    public int getStudentCount() {
        logger.info("Was invoked method to get student count");
        return studentRepository.getStudentCount();
    }

    public double getAverageAge() {
        logger.info("Was invoked method to get average age of students");
        return studentRepository.getAverageAge();
    }

    public List<Student> getLastFiveStudents() {
        logger.debug("Was invoked method to get last five students");
        return studentRepository.findLastFiveStudents();
    }
}
