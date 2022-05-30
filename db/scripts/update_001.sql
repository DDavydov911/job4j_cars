create table if not exists drivers(
    id serial primary key
);

create table if not exists engines(
    id serial primary key
);

create table if not exists cars(
    id serial primary key,
    mark varchar (70),
    bodyType varchar (70),
    description varchar (255),
    sold boolean,
    engine_id integer not null unique references engines(id)
);

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
