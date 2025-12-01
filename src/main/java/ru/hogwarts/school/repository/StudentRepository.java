package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAgeBetween(Long minAge, Long maxAge);

    Faculty getFacultyByStudentId (Long studentId);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer getQuantityOfStudents();

    @Query(value = "select AVG(s.age) as Average_age from student s", nativeQuery = true)
    Float getAverageAgeOfStudents();

    @Query(value = "select * from student s order by  s.student_id desc limit 5", nativeQuery = true)
    List<Student> getFiveLastStudents();
}
