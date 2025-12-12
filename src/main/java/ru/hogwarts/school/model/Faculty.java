package ru.hogwarts.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "color"})})
public class Faculty {

    @Id
    @GeneratedValue (strategy = IDENTITY)
    private Long facultyId;

    private String name;
    private String color;

    @JsonIgnore
    @OneToMany (mappedBy = "faculty")
    private Collection<Student> students;

    public Long getId() {
        return facultyId;
    }

    public void setId(Long id) {
        this.facultyId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Collection<Student> getStudents() {
        return students;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Faculty faculty)) return false;
        return Objects.equals(facultyId, faculty.facultyId) && Objects.equals(name, faculty.name) && Objects.equals(color, faculty.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facultyId, name, color);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + facultyId +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
