package ru.hogwarts.school.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}") // GET http://localhost:8080/student/1
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/faculty/{id}") // GET http://localhost:8080/student/faculty/1
    public ResponseEntity<Faculty> getFacultyByStudentId(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null || studentService.getFacultyByStudentId(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentService.getFacultyByStudentId(id));
    }

    @PostMapping // POST http://localhost:8080/student
    @JsonFormat
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping // PUT http://localhost:8080/student
    @JsonFormat
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}") // DELETE http://localhost:8080/student/1
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> filterStudentsByAgeAndAgeBetween(@RequestParam(required = false) Integer age,
                                                                                @RequestParam(required = false) Long minAge,
                                                                                @RequestParam(required = false) Long maxAge) {
        if (age != null) {
            if (age > 0) {
                return ResponseEntity.ok(studentService.filterStudentsByAge(age));
            }
        }
        if (maxAge != null && minAge != null) {
            if (minAge > 0 && maxAge > 0 && maxAge > minAge) {
                return ResponseEntity.ok(studentService.findByAgeBetween(minAge, maxAge));
            }
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Student>> allStudents() {
        if (!studentService.allStudents().isEmpty()) {
            return ResponseEntity.ok(studentService.allStudents());
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
}
