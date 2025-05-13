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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class FacultyControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/faculty";
    }

    @Test
    public void testAddGetEditDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long id = Objects.requireNonNull(postResponse.getBody()).getId();

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(getBaseUrl() + "/" + id, Faculty.class);
        assertThat(getResponse.getBody().getName()).isEqualTo("Gryffindor");

        faculty.setId(id);
        faculty.setColor("Scarlet");
        restTemplate.put(getBaseUrl(), faculty);

        ResponseEntity<Faculty> updated = restTemplate.getForEntity(getBaseUrl() + "/" + id, Faculty.class);
        assertThat(updated.getBody().getColor()).isEqualTo("Scarlet");

        restTemplate.delete(getBaseUrl() + "/" + id);
        ResponseEntity<Faculty> deleted = restTemplate.getForEntity(getBaseUrl() + "/" + id, Faculty.class);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetFacultiesByColor() {

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(getBaseUrl() + "/color/Red", Faculty[].class);
        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    @Test
    public void testSearchFaculties() {
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(getBaseUrl() + "/search?query=Gryffindor", Faculty[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetStudentsByFacultyId() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(getBaseUrl() + "/1/students", Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student[] students = response.getBody();
        assertThat(students).isNotNull();
        assertThat(students.length).isGreaterThan(0);
        for (Student s : students) {
            System.out.println("Студент: " + s.getName() + ", возраст: " + s.getAge());
        }
    }

}
