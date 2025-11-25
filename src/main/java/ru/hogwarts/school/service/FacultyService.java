package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty (Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long facultyID) {
        return facultyRepository.findById(facultyID).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty (long facultyID) {
        facultyRepository.deleteById(facultyID);
    }

    public Collection<Faculty> filterFacultyByColor(String color) {
        return facultyRepository.findAll().stream()
                .filter(s -> Objects.equals(s.getColor(), color))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Collection<Faculty> allFaculties () {
        return facultyRepository.findAll();
    }

    public Faculty findFacultyByNameOrColorIgnoreCase(String name, String color) {
        return facultyRepository.findFacultyByNameOrColorIgnoreCase(name, color);
    }

    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        return facultyRepository.getStudentsByFacultyId(facultyId);
    }

}
