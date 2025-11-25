package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Faculty findFacultyByNameOrColorIgnoreCase(String name, String color);

    Collection<Student> getStudentsByFacultyId(Long facultyID);

}
