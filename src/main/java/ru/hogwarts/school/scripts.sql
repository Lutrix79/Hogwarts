select * from student;

select s.name, s.age  from student s
where s.age > 10 and s.age < 20;

select s.name from student s;

select * from student s
where s."name" like '%о%';

select * from student s
where s.age < s.id;

select * from student s
order by age;

UPDATE student SET version = 0 WHERE version IS NULL;

ALTER TABLE student ADD COLUMN version INTEGER DEFAULT 0;