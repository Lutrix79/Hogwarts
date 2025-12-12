package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerIntegrationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private StudentController studentController;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() throws Exception {
		Assertions.assertThat(studentController).isNotNull();
	}

	@Test
	public void testGetStudentById() throws Exception{
		final long id = 1;
		Assertions
				.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + id, String.class))
				.isNotNull();
	}

	@Test
	public void testGetFacultyByStudentId() throws Exception{
		final long id = 1;
		Assertions
				.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/faculty/" + id, String.class))
				.isNotNull();
	}

	@Test
	public void testCreateStudent() throws Exception {
		Student student = new Student();
		student.setName("Тестов Тест Тестович");
		student.setAge(123);
		Assertions
				.assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student/", student, String.class))
				.isNotNull();
	}

	@Test
	public void testEditStudent() throws Exception {
		Student student = new Student();
		student.setName("Тестов Тест Тестович");
		student.setAge(123);
		student.setId(1L);
		ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:" + port + "/student/",
				HttpMethod.PUT,
				new HttpEntity<>(student),
				String.class
		);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getStatusCode()).isNotNull();
	}

	@Test
	public void testDeleteStudent() throws Exception {
		final long id = 1;
		Assertions
				.assertThat(this.restTemplate.exchange("http://localhost:" + port + "/student/" + id, HttpMethod.DELETE, null, String.class).getStatusCode())
				.isEqualTo(HttpStatus.OK)
				.isNotNull();
	}

	@Test
	void testFilterBySingleAge() {
		String url = "http://localhost:" + port + "/students?age=20";
		ResponseEntity<Collection<Student>> response = restTemplate.exchange(
				url,
				org.springframework.http.HttpMethod.GET,
				null,
                new ParameterizedTypeReference<>() {
                });
		Assertions.assertThat(response.getStatusCode()).isNotNull();
	}

	@Test
	public void testFilterStudentsByAgeRange() throws Exception {
		String url ="http://localhost:" + port + "/students?minAge=11&maxAge=13";
				ResponseEntity<Collection<Student>> response = restTemplate.exchange(
						url,
						org.springframework.http.HttpMethod.GET,
						null,
                        new ParameterizedTypeReference<>() {
                        });

		Assertions.assertThat(response.getStatusCode()).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).hasSizeGreaterThanOrEqualTo(1);
	}

	@Test
	void testAllStudents() throws Exception {
		String url = "http://localhost:" + port + "/students/all";

		ResponseEntity<Collection<Student>> response = restTemplate.exchange(
				url,
				org.springframework.http.HttpMethod.GET,
				null,
                new ParameterizedTypeReference<>() {
                });

		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody()).isNotNull()
				.as("Коллекция студентов")
				.isNotEmpty();
	}
}
