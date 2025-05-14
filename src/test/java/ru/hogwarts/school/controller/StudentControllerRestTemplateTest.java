package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Objects;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerRestTemplateTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/student";
    }

    @Test
    public void testAddGetEditDeleteStudent() {
        Student student = new Student();
        student.setName("Harry");
        student.setAge(15);

        ResponseEntity<Student> postResponse = restTemplate.postForEntity(getBaseUrl(), student, Student.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long id = Objects.requireNonNull(postResponse.getBody()).getId();

        ResponseEntity<Student> getResponse = restTemplate.getForEntity(getBaseUrl() + "/" + id, Student.class);
        assertThat(getResponse.getBody().getName()).isEqualTo("Harry");

        student.setId(id);
        student.setAge(16);
        restTemplate.put(getBaseUrl(), student);

        ResponseEntity<Student> updated = restTemplate.getForEntity(getBaseUrl() + "/" + id, Student.class);
        assertThat(updated.getBody().getAge()).isEqualTo(16);

        restTemplate.delete(getBaseUrl() + "/" + id);
        ResponseEntity<Student> deleted = restTemplate.getForEntity(getBaseUrl() + "/" + id, Student.class);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetAllStudents() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(getBaseUrl(), Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetStudentsByAge() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(getBaseUrl() + "/age/15", Student[].class);
        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetStudentsByAgeBetween() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(getBaseUrl() + "/by-age-between?min=10&max=20", Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetFacultyByStudentId() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl() + "/1/faculty", Faculty.class);
        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);
    }
}