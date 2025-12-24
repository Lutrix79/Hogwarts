package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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

    public void printParallelAllNamesOfStudents() {
        long startTime = System.nanoTime(); //Start of time measurement
        List<Student> studentList = studentRepository.findAll().stream().toList();
        if (studentList.size() < 6) {
            System.out.println("Добавьте студентов, чтобы их было больше шести");
        } else {
            System.out.println(studentList.get(0).getName());
            System.out.println(studentList.get(1).getName());
            new Thread(() -> {
                System.out.println(studentList.get(2).getName());
                System.out.println(studentList.get(3).getName());
            }).start();
            new Thread(() -> {
                System.out.println(studentList.get(4).getName());
                System.out.println(studentList.get(5).getName());
            }).start();
        }
        long endTime = System.nanoTime(); //End of time measurement
        long fullTimeOfParallel = endTime - startTime; //Execution time in nanos;
        System.out.println("Execution time of synchronized stream in millis " + (double)fullTimeOfParallel/1_000_000 + " ms");
    }

    public void printSynchronizedAllNamesOfStudents() {
        long startTime = System.nanoTime(); //Start of time measurement
        List<Student> studentList = studentRepository.findAll().stream().toList();
        if (studentList.size() < 6) {
            System.out.println("Добавьте студентов, чтобы их было больше шести");
        } else {
            printSynchro(studentList);
        }
        long endTime = System.nanoTime(); //End of time measurement
        long fullTimeOfParallel = endTime - startTime; //Execution time in nanos;
        System.out.println("Execution time of synchronized stream in millis " + (double)fullTimeOfParallel/1_000_000 + " ms");
    }

    private synchronized void printSynchro(List<Student> studentList) {
        System.out.println(studentList.get(0).getName());
        System.out.println(studentList.get(1).getName());
        new Thread(() -> {
            System.out.println(studentList.get(2).getName());
            System.out.println(studentList.get(3).getName());
        }).start();
        new Thread(() -> {
            System.out.println(studentList.get(4).getName());
            System.out.println(studentList.get(5).getName());
        }).start();
    }

    public long calculateFastestStream() {

        long startTime = System.nanoTime(); //Start of time measurement
        long sum = LongStream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, Long::sum);
        long endTime = System.nanoTime(); //End of time measurement
        long fullTimeOfParallel1 = endTime - startTime; //Execution time in nanos;
        System.out.println("Execution time of parallel stream 1 in millis " + (double)fullTimeOfParallel1/1_000_000 + " ms");

        startTime = System.nanoTime();
        sum = LongStream
                .rangeClosed(1, 1_000_000)
                .parallel()
                .sum();
        endTime = System.nanoTime();
        long fullTimeOfParallel2 = endTime - startTime;
        System.out.println("Execution time of parallel stream 2 in millis " + (double)fullTimeOfParallel2/1_000_000 + " ms");

        startTime = System.nanoTime();
        sum = LongStream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, Long::sum);
        endTime = System.nanoTime();
        long fullTimeOfSerial = endTime - startTime;
        System.out.println("Execution time of serial stream 1 in millis " + (double)fullTimeOfSerial/1_000_000 + " ms");

        long minTime = Math.min(fullTimeOfParallel1, Math.min(fullTimeOfParallel2, fullTimeOfSerial));
        if (minTime == fullTimeOfParallel1) {
            System.out.println("""
                    Fastest way is parallel stream 1: LongStream
                                    .iterate(1, a -> a + 1)
                                    .limit(1_000_000)
                                    .parallel()
                                    .reduce(0, Long::sum);""");
        } else if (minTime == fullTimeOfParallel2) {
            System.out.println("""
                    Fastest way is parallel stream 2: LongStream
                                    .rangeClosed(1, 1_000_000)
                                    .parallel()
                                    .sum();""");
        } else {
            System.out.println("""
                    Fastest way is serial stream: LongStream.iterate(1, a -> a + 1)
                                    .limit(1_000_000)
                                    .reduce(0, Long::sum);""");
        }

        return sum;
    }
}
