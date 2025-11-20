package ru.hogwarts.school.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}") // GET http://localhost:8080/faculty/1
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultyService.getFaculty(id);
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/students/{id}") // GET http://localhost:8080/faculty/students/1
    public ResponseEntity<Collection<Student>> allStudentsFaculty(@PathVariable Long id) {
        if (!facultyService.getFaculty(id).getStudents().isEmpty()) {
            return ResponseEntity.ok(facultyService.getStudentsByFacultyId(id));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping // POST http://localhost:8080/faculty
    @JsonFormat
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping // PUT http://localhost:8080/faculty
    @JsonFormat
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(faculty);
    }

    @DeleteMapping("{id}") // DELETE http://localhost:8080/faculty/1
    public ResponseEntity deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Faculty> findFacultyByNameOrColorIgnoreCase(@RequestParam(required = false) String name,
                                                                          @RequestParam(required = false) String color) {
        if ((color != null && !color.isBlank()) || (name != null && !name.isBlank())) {
            return ResponseEntity.ok(facultyService.findFacultyByNameOrColorIgnoreCase(name, color));
        }
       return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Faculty>> allFaculties() {
        if (!facultyService.allFaculties().isEmpty()) {
            return ResponseEntity.ok(facultyService.allFaculties());
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
}
