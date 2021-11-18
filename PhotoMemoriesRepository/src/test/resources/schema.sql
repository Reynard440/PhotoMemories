create schema local_photo_memories_db;

create table photo
(
    photo_id         int auto_increment
        primary key,
    ph_modified_date date         not null,
    ph_capturedby    varchar(255) not null,
    ph_format        varchar(255) not null,
    ph_link          varchar(255) not null,
    ph_location      varchar(255) null,
    ph_name          varchar(255) not null,
    ph_size          double       null,
    ph_upload_date   date         not null
);

create table user
(
    user_id            int auto_increment
        primary key,
    user_email         varchar(150) null,
    user_fname         varchar(100) not null,
    user_lname         varchar(100) not null,
    user_cell_nr       varchar(10)  not null,
    user_hash_password varchar(255) not null,
    user_join_date     date         not null,
    constraint UK_9kfdqio86ctkgf9tj7g68o0y6
        unique (user_cell_nr),
    constraint UK_j09k2v8lxofv2vecxu2hde9so
        unique (user_email)
);

create table shared
(
    shared_id      int auto_increment
        primary key,
    sh_shared_date date not null,
    sh_has_access  bit  not null,
    sh_shared_with int  not null,
    photo_id       int  null,
    user_id        int  null,
    constraint FK79av7kswx8hb986mo3dfsf7g7
        foreign key (photo_id) references photo (photo_id)
            on update cascade on delete cascade,
    constraint FKogluef9dmg314tlqxffefqd1u
        foreign key (user_id) references user (user_id)
            on update cascade on delete cascade
);
