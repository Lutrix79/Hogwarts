package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent (Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        logger.info("Was invoked method for get student");
        return studentRepository.findById(id).orElse(new Student());
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        return studentRepository.save(student);
    }

    public void deleteStudent (long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Collection<Student> filterStudentsByAge(int age) {
        logger.info("Was invoked method for filter students by age");
        return studentRepository.findAll().stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Collection<Student> allStudents () {
        logger.info("Was invoked method for find all students");
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeBetween(Long minAge, Long maxAge) {
        logger.info("Was invoked method for find students by range of ages");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty getFacultyByStudentId(Long studentId) {
        logger.info("Was invoked method for getting faculty of student");
        Student student = studentRepository.getReferenceById(studentId);
        return student.getFaculty();
    }

    public Integer getQuantityOfStudents() {
        logger.info("Was invoked method for get quantity of students");
        return studentRepository.getQuantityOfStudents();
    }

    public Float getAverageAgeOfStudents() {
        logger.info("Was invoked method for get average age of students");
        return studentRepository.getAverageAgeOfStudents();
    }
    
    public List<Student> getFiveLastStudents() {
        logger.info("Was invoked method for get five last student");
        return studentRepository.getFiveLastStudents();
    }
}
