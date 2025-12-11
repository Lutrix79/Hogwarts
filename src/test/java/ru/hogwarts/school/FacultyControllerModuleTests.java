package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FacultyController.class)
public class FacultyControllerModuleTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    public void testGetFacultyById_WhenFacultyExists_ShouldReturnFaculty() throws Exception {
        // Arrange (Подготовка)
        Long id = 1L;
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName("Когтевран");
        faculty.setColor("синий");

        // Подготавливаем поведение мока
        when(facultyService.getFaculty(id)).thenReturn(faculty);

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                // Assert (Проверка)
                .andExpect(status().isOk())                               // Должен вернуть 200 OK
                .andExpect(jsonPath("$.name").value("Когтевран"))       // Проверяем имя
                .andExpect(jsonPath("$.color").value("синий"));           // Проверяем цвет
    }

    @Test
    public void testGetFacultyById_WhenFacultyDoesNotExist_ShouldReturnBadRequest() throws Exception {
        // Arrange (Подготовка)
        long id = 999L;

        // Подготавливаем поведение мока
        when(facultyService.getFaculty(id)).thenReturn(null);

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                // Assert (Проверка)
                .andExpect(status().isBadRequest());                      // Должен вернуть 400 Bad Request
    }

    @Test
    public void testGetStudentsOfFaculty_WhenStudentsExist_ShouldReturnStudents() throws Exception {
        // Arrange (Подготовка)
        Long id = 1L;
        Student s1 = new Student();
        Student s2 = new Student();
        s1.setAge(25); s1.setName("Тест1"); s1.setId(1L);
        s2.setAge(28); s2.setName("Тест2"); s2.setId(2L);
        Collection<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);

        // Подготавливаем поведение мока
        when(facultyService.getStudentsByFacultyId(id)).thenReturn(students);

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/students/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                // Assert (Проверка)
                .andExpect(status().isOk())                               // Должен вернуть 200 OK
                .andExpect(jsonPath("$.[*].size()", is(2)))              // Проверяем размер массива
                .andExpect(jsonPath("$.[*].firstName", containsInAnyOrder("Тест1", "Тест2")));
    }

    @Test
    public void testGetStudentsOfFaculty_WhenNoStudentsExist_ShouldReturnEmptyList() throws Exception {
        // Arrange (Подготовка)
        Long id = 1L;

        // Подготавливаем поведение мока
        when(facultyService.getStudentsByFacultyId(id)).thenReturn(List.of());

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/students/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                // Assert (Проверка)
                .andExpect(status().isOk())                               // Должен вернуть 200 OK
                .andExpect(jsonPath("$.*", hasSize(0)));                 // Проверяем пустой массив
    }

    @Test
    public void testCreateFaculty_MissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        // Arrange (Подготовка)
        Faculty faculty = new Faculty();
        faculty.setName(null); // Пропущенное обязательное поле

        // Подготавливаем поведение мока
        when(facultyService.createFaculty(faculty)).thenReturn(null);

        // Convert the faculty object into JSON string
        String jsonContent = new ObjectMapper().writeValueAsString(faculty);

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                // Assert (Проверка)
                .andExpect(status().isBadRequest());                      // Должен вернуть 400 Bad Request
    }

    @Test
    public void testEditFaculty_ValidData_ShouldReturnEditedFaculty() throws Exception {
        // Arrange (Подготовка)
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        // Подготавливаем поведение мока
        when(facultyService.editFaculty(faculty)).thenReturn(faculty);

        // Convert the faculty object into JSON string
        String jsonContent = new ObjectMapper().writeValueAsString(faculty);

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                // Assert (Проверка)
                .andExpect(status().isOk())                               // Должен вернуть 200 OK
                .andExpect(jsonPath("$.name").value("Гриффиндор"))           // Проверяем имя
                .andExpect(jsonPath("$.color").value("красный"));           // Проверяем цвет
    }

    @Test
    public void testEditFaculty_InvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange (Подготовка)
        Faculty faculty = new Faculty();
        faculty.setId(999L); // Незаконный ID
        faculty.setName("Гриффендуй");
        faculty.setColor("серобурмалиновы");

        // Подготавливаем поведение мока
        when(facultyService.editFaculty(faculty)).thenReturn(null);

        // Convert the faculty object into JSON string
        String jsonContent = new ObjectMapper().writeValueAsString(faculty);

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                // Assert (Проверка)
                .andExpect(status().isBadRequest());                      // Должен вернуть 400 Bad Request
    }

    @Test
    public void testDeleteFaculty_ValidId_ShouldReturnOK() throws Exception {
        // Arrange (Подготовка)
        long id = 1L;

        // Подготавливаем поведение мока
        doNothing().when(facultyService).deleteFaculty(id);

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/{id}", id))
                // Assert (Проверка)
                .andExpect(status().isOk());                              // Должен вернуть 200 OK
    }

    @Test
    public void testDeleteFaculty_NonexistentId_ShouldReturnOK() throws Exception {
        // Arrange (Подготовка)
        long id = 999L;

        // Подготавливаем поведение мока
        doNothing().when(facultyService).deleteFaculty(id);

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/{id}", id))
                // Assert (Проверка)
                .andExpect(status().isOk());                              // Должен вернуть 200 OK
    }

    @Test
    public void testFindFacultyByNameOrColor_ValidSearchCriteria_ShouldReturnFaculty() throws Exception {
        // Arrange (Подготовка)
        Faculty faculty = new Faculty();
        faculty.setName("Пуффендуй");
        faculty.setColor("желтый");

        // Подготавливаем поведение мока
        when(facultyService.findFacultyByNameOrColorIgnoreCase("Пуффендуй", "желтый")).thenReturn(faculty);

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty?name=Пуффендуй")
                        .accept(MediaType.APPLICATION_JSON))
                // Assert (Проверка)
                .andExpect(status().isOk())                               // Должен вернуть 200 OK
                .andExpect(jsonPath("$.name").value("Пуффендуй"))           // Проверяем имя
                .andExpect(jsonPath("$.color").value("желтый"));           // Проверяем цвет
    }

    @Test
    public void testFindFacultyByNameOrColor_InvalidSearchCriteria_ShouldReturnNotFound() throws Exception {
        // Arrange (Подготовка)
        // Подготавливаем поведение мока
        when(facultyService.findFacultyByNameOrColorIgnoreCase("Unknown", "uncolor")).thenReturn(null);

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty?name=Unknown")
                        .accept(MediaType.APPLICATION_JSON))
                // Assert (Проверка)
                .andExpect(status().isNotFound());                        // Должен вернуть 404 Not Found
    }

    @Test
    public void testGetAllFaculties_WhenFacultiesExist_ShouldReturnFaculties() throws Exception {
        // Arrange (Подготовка)
        Faculty f1 = new Faculty();
        Faculty f2 = new Faculty();
        f1.setColor("белый"); f1.setName("Тест1"); f1.setId(1L);
        f1.setColor("черный"); f2.setName("Тест2"); f2.setId(2L);
        Collection<Faculty> faculties = new ArrayList<>();
        faculties.add(f1);
        faculties.add(f2);

        // Подготавливаем поведение мока
        when(facultyService.allFaculties()).thenReturn(faculties);

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/all")
                        .accept(MediaType.APPLICATION_JSON))
                // Assert (Проверка)
                .andExpect(status().isOk())                               // Должен вернуть 200 OK
                .andExpect(jsonPath("$.[*].size()", is(2)))              // Проверяем длину массива
                .andExpect(jsonPath("$.[*].name", containsInAnyOrder("Тест1", "Тест2")));
    }

    @Test
    public void testGetAllFaculties_WhenNoFacultiesExist_ShouldReturnEmptyList() throws Exception {
        // Arrange (Подготовка)
        // Подготавливаем поведение мока
        when(facultyService.allFaculties()).thenReturn(List.of());

        // Act (Выполнение)
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/all")
                        .accept(MediaType.APPLICATION_JSON))
                // Assert (Проверка)
                .andExpect(status().isOk())                               // Должен вернуть 200 OK
                .andExpect(jsonPath("$.*", hasSize(0)));                 // Проверяем пустой массив
    }
}