CREATE TABLE people (
    person_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    license BOOLEAN NOT NULL
);

CREATE TABLE cars (
    car_id SERIAL PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    price NUMERIC(10,2) NOT NULL
);

CREATE TABLE car_people (
    person_id INTEGER REFERENCES people(person_id),
    car_id INTEGER REFERENCES cars(car_id),
    PRIMARY KEY (person_id, car_id)
);