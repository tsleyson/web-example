create table users(
       user_id serial primary key,
       username varchar(50),
       email varchar(50),
       password_salt bytea,
       hashed_password bytea
);             

create table blogs (
       post_id serial primary key,
       auth_name varchar(50) references users(username),
       post_date date,
       post_time time,
       post_text text
);
