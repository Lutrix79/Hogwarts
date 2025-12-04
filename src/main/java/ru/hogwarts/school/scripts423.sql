SELECT student.name AS student_name, student.age, faculty.name AS faculty_name
FROM student
INNER JOIN faculty ON student.faculty_id = faculty.faculty_id;

SELECT student.name AS student_name, student.age, avatar.file_path
FROM student
INNER JOIN avatar ON student.student_id = avatar.student_student_id;