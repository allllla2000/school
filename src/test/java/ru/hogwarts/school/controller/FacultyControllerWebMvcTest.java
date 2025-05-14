package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    public void shouldReturnFacultyById() throws Exception {
        Mockito.when(facultyService.findFaculty(1L)).thenReturn(new Faculty(1L, "Gryffindor", "Red"));

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"));
    }

    @Test
    public void shouldReturnNotFoundFaculty() throws Exception {
        Mockito.when(facultyService.findFaculty(999L)).thenReturn(null);

        mockMvc.perform(get("/faculty/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldSearchFacultyByColor() throws Exception {
        Mockito.when(facultyService.getFacultiesByColor("Red"))
                .thenReturn(Collections.singletonList(new Faculty(1L, "Gryffindor", "Red")));

        mockMvc.perform(get("/faculty/color/Red"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNotFoundIfColorEmpty() throws Exception {
        Mockito.when(facultyService.getFacultiesByColor("Invisible"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/faculty/color/Invisible"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetStudentsByFacultyId() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        Student student = new Student(1L, "Harry", 15);
        student.setFaculty(faculty);

        Mockito.when(studentRepository.findByFacultyId(1L)).thenReturn(List.of(student));

        mockMvc.perform(get("/faculty/1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Harry"))
                .andExpect(jsonPath("$[0].age").value(15));
    }
}
