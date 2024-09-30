create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

CREATE SEQUENCE "SEQ_AUTHORS"
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 1
    NOCACHE
    NOCYCLE;

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

CREATE SEQUENCE "SEQ_GENRES"
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 1
    NOCACHE
    NOCYCLE;

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

CREATE SEQUENCE "SEQ_BOOKS"
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 1
    NOCACHE
    NOCYCLE;

create table comments (
    id bigserial,
    book_id bigint references books (id) on delete cascade,
    text varchar(255)
);

CREATE SEQUENCE "SEQ_COMMENTS"
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 1
    NOCACHE
    NOCYCLE;