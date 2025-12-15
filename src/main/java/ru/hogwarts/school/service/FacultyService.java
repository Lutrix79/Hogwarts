package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty (Faculty faculty) {
       logger.info("Was invoked method for create faculty");
       return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long facultyID) {
        logger.info("Was invoked method for get faculty");
        return facultyRepository.findById(facultyID).orElse(new Faculty());
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty (long facultyID) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(facultyID);
    }

    public Collection<Faculty> filterFacultyByColor(String color) {
        logger.info("Was invoked method for filter faculty by color");
        return facultyRepository.findAll().stream()
                .filter(s -> Objects.equals(s.getColor(), color))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Collection<Faculty> allFaculties () {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    public Faculty findFacultyByNameOrColorIgnoreCase(String name, String color) {
        logger.info("Was invoked method for get faculties by name or color");
        return facultyRepository.findFacultyByNameOrColorIgnoreCase(name, color);
    }

    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        logger.info("Was invoked method for get all students on faculty");
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElse(Collections.emptyList());
    }
}
