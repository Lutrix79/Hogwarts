package ru.hogwarts.school.model;

import jakarta.persistence.*;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Student {

    @Id
    @GeneratedValue (strategy = IDENTITY)
    private Long studentId;

    private String name;
    private int age;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "faculty_id")
    private Faculty faculty;

    public Long getId() {
        return studentId;
    }

    public void setId(Long id) {
        this.studentId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Student student)) return false;
        return age == student.age && Objects.equals(studentId, student.studentId) && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, name, age);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + studentId +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
