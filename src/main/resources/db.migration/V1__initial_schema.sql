create sequence hibernate_sequence start 1 increment 1;

create table publication (
    id int8 not null,
    filename varchar(255),
    tag varchar(255),
    text varchar(2048) not null,
    user_id int8,
    post_time timestamp,
    last_view timestamp,
    primary key (id)
);

create table message (
    id int8 not null,
    text varchar(2048) not null,
    user_id int8,
    publication_id int8,
    send_time timestamp,
    primary key (id)
);

create table user_role (
    user_id int8 not null,
    roles varchar(255)
);

create table usr (
    id int8 not null,
    activation_code varchar(255),
    active boolean not null,
    email varchar(255),
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
);

alter table if exists publication
    add constraint publication_user_fk
    foreign key (user_id) references usr;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;

alter table if exists user_publication
    add constraint user_publication_user_id_fk
    foreign key (user_id) references usr;

alter table if exists user_publication
    add constraint user_publication_publication_id_fk
    foreign key (publication_id) references publication;