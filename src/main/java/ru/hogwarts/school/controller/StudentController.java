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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Getting student info by ID
     */
    @GetMapping("/{id}") // GET http://localhost:8080/student/1
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);
    }

    /**
     * Getting faculty by student's ID
     */
    @GetMapping("/faculty/{id}") // GET http://localhost:8080/student/faculty/1
    public ResponseEntity<Faculty> getFacultyByStudentId(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null || studentService.getFacultyByStudentId(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentService.getFacultyByStudentId(id));
    }


    /**
     * Just my comrade method
     */
//@Operation(summary = "Посмотреть факультет студента")
//@GetMapping("{id}/faculty")
//public ResponseEntity<Faculty> getFacultyByStudent(@PathVariable Long id) {
//    Student student = studentService.findStudent(id);
//    if (student == null) {
//        return ResponseEntity.notFound().build();
//    }
//    Faculty faculty = student.getFaculty();
//    if (faculty == null) {
//        return ResponseEntity.notFound().build();
//    }
//    return ResponseEntity.ok(faculty);
//}

    /**
     * Create student
     */
    @PostMapping // POST http://localhost:8080/student
    @JsonFormat
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    /**
     * Edit existing student or return flag of bad request if student not exist
     */
    @PutMapping // PUT http://localhost:8080/student
    @JsonFormat
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);
    }

    /**
     * Delete student
     */
    @DeleteMapping("/{id}") // DELETE http://localhost:8080/student/1
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Filter Students By Age And Age Between maxAge and minAge
     */
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

    /**
     * Return collection of all students
     */
    @GetMapping("/all")
    public ResponseEntity<Collection<Student>> allStudents() {
        if (!studentService.allStudents().isEmpty()) {
            return ResponseEntity.ok(studentService.allStudents());
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    /**
     * Return all students, whose names began with certain substring
     */
    @GetMapping("/{beginSubstring}")
    public ResponseEntity<Collection<Student>> allStudentsStartingWithSubstring(@RequestParam String substring) {
        if (!studentService.allStudents().isEmpty()) {
            return ResponseEntity.ok(
                    studentService.allStudents().stream()
                    .filter(s -> s.getName().toLowerCase().startsWith(substring))
                    .sorted()
                    .collect(Collectors.toCollection(() -> Collections.synchronizedCollection(new LinkedHashSet<>())))
            );
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    /**
     * Calculate average age of all students by stream
     */
    @GetMapping("/average-age-students-by-stream")
    public double getAverageAgeOfStudentsByStream() {
        return studentService.allStudents().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    /**
     * Calculate sum by parallel and serial streams and find the fastest way
     */
    @GetMapping("/parallel")
    public long getSum() {
        return studentService.calculateFastestStream();
    }

    /**
     * Print name of students, using parallel threads
     */
    @GetMapping("/students/print-parallel")
    public void printParallelAllNamesOfStudents() {
        studentService.printParallelAllNamesOfStudents();
    }

    /**
     * Print name of students, using synchronized parallel threads
     */
    @GetMapping("/students/print-synchronized")
    public void printSynchronizedAllNamesOfStudents() {
        studentService.printSynchronizedAllNamesOfStudents();
    }

    /**
     * Get quantity of students, using query to DB
     */
    @GetMapping("/total-quantity-students")
    public Integer getQuantityOfStudents() {
        return studentService.getQuantityOfStudents();
    }

    /**
     * Get average age of all students, using query to DB
     */
    @GetMapping("/average-age-students")
    public Float getAverageAgeOfStudents() {
        return studentService.getAverageAgeOfStudents();
    }

    /**
     * Get five last students, using query to DB
     */
    @GetMapping("/five-last-students")
    public List<Student> getFiveLastStudents() {
        return studentService.getFiveLastStudents();
    }
}
