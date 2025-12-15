-- liquibase formatted sql

-- changeset ilukin:1
CREATE INDEX student_name_index ON student (name)

-- changeset ilukin:2
CREATE INDEX faculty_name_color_index ON faculty (name, color)