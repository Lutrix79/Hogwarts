package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(StudentController.class)
public class StudentControllerModuleTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    public void testGetStudentInfo_WhenStudentExists_ShouldReturnStudent() throws Exception {
        Long id = 1L;
        Student expectedStudent = new Student();
        expectedStudent.setId(id);
        expectedStudent.setName("Тестов Тест Тестович");
        expectedStudent.setAge(15);

        when(studentService.getStudent(id)).thenReturn(expectedStudent);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Тестов Тест Тестович"))
                .andExpect(jsonPath("$.age").value(15));
    }

    @Test
    public void testGetFacultyByStudentId_WhenStudentAndFacultyExist_ShouldReturnFaculty() throws Exception {
        // Arrange (Подготовка)
        Long id = 1L;
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффендуй");
        Student student = new Student();
        student.setId(id);
        student.setName("Тестов Тест Тестович");
        student.setAge(15);

        when(studentService.getStudent(id)).thenReturn(student);
        when(studentService.getFacultyByStudentId(id)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/faculty/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())                      // Должен вернуть 200 OK
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффендуй"));
    }

    @Test
    public void testCreateStudent_ValidData_ShouldReturnCreatedStudent() throws Exception {
        Student student = new Student();
        student.setName("Тестов Тест Тестович");
        student.setAge(15);

        when(studentService.createStudent(student)).thenReturn(student);

        String jsonContent = new ObjectMapper().writeValueAsString(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())                       // Должен вернуть 200 OK
                .andExpect(jsonPath("$.name").value("Тестов Тест Тестович")) // Проверяем имя
                .andExpect(jsonPath("$.age").value(15));          // Проверяем возраст
    }

    @Test
    public void testEditStudent_SuccessfulUpdate_ReturnsUpdatedStudent() throws Exception {
        Student originalStudent = new Student();
        originalStudent.setId(1L);
        originalStudent.setName("Тестов Тест Тестович");
        originalStudent.setAge(15);

        Student updatedStudent = new Student();
        updatedStudent.setId(originalStudent.getId());
        updatedStudent.setName("Тестидзе Тестелло");
        updatedStudent.setAge(30);

        when(studentService.editStudent(updatedStudent)).thenReturn(updatedStudent);

        String jsonContent = new ObjectMapper().writeValueAsString(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders.put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Тестидзе Тестелло"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    public void testEditStudent_NonexistentStudent_ReturnsBadRequest() throws Exception {
        Student invalidStudent = new Student();
        invalidStudent.setId(999L);
        invalidStudent.setName("Никто Некто Нектович");
        invalidStudent.setAge(0);

        when(studentService.editStudent(invalidStudent)).thenReturn(null);

        String jsonContent = new ObjectMapper().writeValueAsString(invalidStudent);

        mockMvc.perform(MockMvcRequestBuilders.put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());                // Должен вернуть 400 Bad Request
    }

    @Test
    public void testDeleteStudent_ExistingStudent_ReturnsOK() throws Exception {
        long existingId = 1L;

        doNothing().when(studentService).deleteStudent(existingId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}", existingId))
               .andExpect(status().isOk());
    }

    @Test
    public void testDeleteStudent_NonexistentStudent_ReturnsOK() throws Exception {
        long nonexistentId = 999L;

        doNothing().when(studentService).deleteStudent(nonexistentId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}", nonexistentId))
               .andExpect(status().isOk());
    }

    @Test
    public void testFilterStudentsBySingleAge_ReturnsFilteredStudents() throws Exception {
        // Arrange (Подготовка)
        int age = 15;
        Student s1 = new Student();
        Student s2 = new Student();
        s1.setAge(15); s1.setName("Тест1"); s1.setId(1L);
        s2.setAge(15); s2.setName("Тест2"); s2.setId(2L);
        Collection<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);

        when(studentService.filterStudentsByAge(age)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/students?age=" + age)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].size()", is(2)))
                .andExpect(jsonPath("$.[*].age", containsInAnyOrder(15, 15)));
    }

    @Test
    public void testFilterStudentsByAgeRange_ReturnsFilteredStudents() throws Exception {
        Long minAge = 20L;
        Long maxAge = 30L;
        Student s1 = new Student();
        Student s2 = new Student();
        s1.setAge(25); s1.setName("Тест1"); s1.setId(1L);
        s2.setAge(28); s2.setName("Тест2"); s2.setId(2L);
        Collection<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);

        when(studentService.findByAgeBetween(minAge, maxAge)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/students?minAge=" + minAge + "&maxAge=" + maxAge)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].size()", is(2)))
                .andExpect(jsonPath("$.[*].age", containsInAnyOrder(25, 28)));
    }

    @Test
    public void testAllStudents_WhenStudentsExist_ShouldReturnStudents() throws Exception {
        Student s1 = new Student();
        Student s2 = new Student();
        s1.setAge(15); s1.setName("Тест1"); s1.setId(1L);
        s2.setAge(18); s2.setName("Тест2"); s2.setId(2L);
        Collection<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);

        when(studentService.allStudents()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/all")
                        .accept(MediaType.APPLICATION_JSON))
                // Assert (Проверка)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andExpect(jsonPath("$.[*].name", containsInAnyOrder("Тест1", "Тест2")));
    }

    @Test
    public void testAllStudents_WhenNoStudentsExist_ShouldReturnEmptyList() throws Exception {
        when(studentService.allStudents()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/students/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }
}
