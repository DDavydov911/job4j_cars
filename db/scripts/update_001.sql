create table if not exists drivers(
    id serial primary key
);

create table if not exists engines(
    id serial primary key
);

create table if not exists cars(
    id serial primary key,
    engine_id integer not null unique references engines(id)
);


create table if not exists history_owner(
    id serial primary key,
    driver_id int not null references drivers(id),
    car_id int not null references cars(id)
);