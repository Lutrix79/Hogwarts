package ru.hogwarts.school;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    /**
     * Тест получения факультета по ID
     */
    @Test
    public void testGetFacultyById() throws Exception {
        // Arrange (Подготовка)
        final long id = 1L;

        // Act (Выполнение)
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + id, String.class);

        // Assert (Проверка)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    /**
     * Тест получения студентов факультета по ID
     */
    @Test
    public void testGetStudentsOfFaculty() throws Exception {
        // Arrange (Подготовка)
        final long id = 1L;

        // Act (Выполнение)
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/students/" + id, String.class);

        // Assert (Проверка)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    /**
     * Тест создания нового факультета
     */
    @Test
    public void testCreateFaculty() throws Exception {
        // Arrange (Подготовка)
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        // Act (Выполнение)
        ResponseEntity<Faculty> response = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        // Assert (Проверка)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    /**
     * Тест редактирования факультета
     */
    @Test
    public void testEditFaculty() throws Exception {
        // Arrange (Подготовка)
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Пуффендуй");
        faculty.setColor("жёлтый");

        // Act (Выполнение)
        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                new HttpEntity<>(faculty),
                Faculty.class
        );

        // Assert (Проверка)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    /**
     * Тест удаления факультета
     */
    @Test
    public void testDeleteFaculty() throws Exception {
        // Arrange (Подготовка)
        final long id = 1L;

        // Act (Выполнение)
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Assert (Проверка)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    /**
     * Тест поиска факультета по имени или цвету
     */
    @Test
    public void testFindFacultyByNameOrColor() throws Exception {
        // Arrange (Подготовка)
        String name = "Когтевран";
        String color = "синий";

        // Act (Выполнение)
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty?name=" + name + "&color=" + color, String.class);

        // Assert (Проверка)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    /**
     * Тест получения всех факультетов
     */
    @Test
    public void testGetAllFaculties() throws Exception {
        // Act (Выполнение)
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/all", String.class);

        // Assert (Проверка)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}