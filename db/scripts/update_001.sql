create table if not exists drivers(
    id serial primary key
);

create table if not exists engines(
    id serial primary key,
    horsepower INTEGER
);

INSERT INTO engines(horsepower) VALUES (100);
INSERT INTO engines(horsepower) VALUES (150);

create table if not exists carmarks(
    id serial primary key,
    `name` varchar (255)
);

INSERT INTO carmarks(name) VALUES('Hyundai');
INSERT INTO carmarks(name) VALUES('Audi');

create table if not exists cars(
    id serial primary key,
    carmark_id INTEGER NOT NULL UNIQUE REFERENCES carmarks(id),
    bodyType varchar (70),
    description varchar (255),
    sold boolean DEFAULT FALSE,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    engine_id integer not null unique references engines(id)
);

INSERT INTO cars(carmark_id, description, engine_id) VALUES(1, 'My horse', 1);
INSERT INTO cars(carmark_id, description, engine_id) VALUES(2, 'Good car', 2);

create table if not exists photos(
    id serial primary key,
    title varchar(255),
    photo bytea,
    car_id integer not null unique references cars(id)
);

create table if not exists history_owner(
    id serial primary key,
    driver_id int not null references drivers(id),
    car_id int not null references cars(id)
);
