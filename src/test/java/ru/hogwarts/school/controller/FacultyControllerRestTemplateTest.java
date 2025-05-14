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
    public void testAddFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getId()).isNotNull();
    }

    @Test
    public void testGetFacultyById() {
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");

        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        Long id = Objects.requireNonNull(postResponse.getBody()).getId();

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(getBaseUrl() + "/" + id, Faculty.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getName()).isEqualTo("Hufflepuff");
    }

    @Test
    public void testEditFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Ravenclaw");
        faculty.setColor("Blue");

        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        Faculty created = postResponse.getBody();
        assert created != null;

        created.setColor("Indigo");
        restTemplate.put(getBaseUrl(), created);

        ResponseEntity<Faculty> updated = restTemplate.getForEntity(getBaseUrl() + "/" + created.getId(), Faculty.class);
        assertThat(updated.getBody().getColor()).isEqualTo("Indigo");
    }

    @Test
    public void testDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Slytherin");
        faculty.setColor("Green");

        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        Long id = Objects.requireNonNull(postResponse.getBody()).getId();

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


}
