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
import java.util.stream.IntStream;

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

    public List<String> getNamesStartingWithA() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name != null && name.toUpperCase().startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAverageAgeOfStudents() {
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }
    public int getParallelSum() {
        return IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
    }


    public void printStudentsInParallel() {
        List<Student> students = getAllStudents();

        if (students.size() < 6) {
            System.out.println("Недостаточно студентов для демонстрации потоков");
            return;
        }

        System.out.println("Основной поток: " + Thread.currentThread().getName());
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            System.out.println("Поток 1: " + Thread.currentThread().getName());
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Поток 2: " + Thread.currentThread().getName());
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        });

        thread1.start();
        thread2.start();
    }

    public void printStudentsSynchronized() {
        List<Student> students = getAllStudents();

        if (students.size() < 6) {
            System.out.println("Недостаточно студентов для демонстрации потоков");
            return;
        }

        printStudentName(students.get(0));
        printStudentName(students.get(1));

        Thread thread1 = new Thread(() -> {
            printStudentName(students.get(2));
            printStudentName(students.get(3));
        });

        Thread thread2 = new Thread(() -> {
            printStudentName(students.get(4));
            printStudentName(students.get(5));
        });

        thread1.start();
        thread2.start();
    }

    private synchronized void printStudentName(Student student) {
        System.out.println(Thread.currentThread().getName() + ": " + student.getName());
    }





}
