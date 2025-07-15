package ru.hogwarts.school.model;

import jakarta.persistence.*;

@Entity
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String filePath;

    public Avatar() {
    }

    public Avatar(String filePath) {
        this.filePath = filePath;
    }

    public Long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}
