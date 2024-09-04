drop table if exists users, categories, compilation, events, event_compilations, requests  cascade;

create table if not exists users (
    id      integer generated always as identity not null,
    name    varchar(128) not null,
    email   varchar(254) not null,

    constraint pk_user primary key (id),
    constraint uq_user_email unique (email)
);

create table if not exists categories (
    id   integer generated always as identity not null,
    name varchar(128) not null,

    constraint pk_category primary key (id),
    constraint uq_category_name unique (name)
);

create table if not exists compilations (
    id     integer generated always as identity not null,
    title  varchar(128) not null,
    pinned boolean,

    constraint pk_compilation primary key (id)
);

create table if not exists events (
    id                 integer generated always as identity not null,
    title              varchar(120) not null,
    annotation         varchar(2000) not null,
    description        varchar(7000) not null,
    event_date         timestamp without time zone not null,
    created_on         timestamp without time zone,
    published_on       timestamp without time zone,
    paid               boolean,
    participant_limit  integer,
    request_moderation boolean,
    state              varchar(50),
    lat                float,
    lon                float,
    initiator_id       integer not null,
    category_id        integer not null,
    views              integer,

    constraint pk_event primary key (id),
    constraint fk_event_user
        foreign key (initiator_id)
            references users (id)
            on delete cascade,
    constraint fk_event_category
        foreign key (category_id)
            references categories (id)
            on delete cascade
);

create table if not exists event_compilations
(
    id             integer generated always as identity not null,
    compilation_id integer not null,
    event_id       integer not null,

    constraint pk_event_compilation primary key (id),
    constraint fk_compilation
        foreign key (compilation_id)
            references compilations (id)
            on delete cascade,
    constraint fk_event
        foreign key (event_id)
            references events (id)
            on delete cascade
);

create table if not exists requests (
    id           integer generated always as identity not null,
    requester_id integer not null,
    event_id     integer not null,
    created      timestamp without time zone not null,
    status       varchar(50) not null,

    constraint pk_request primary key (id),
    constraint fk_request_user
        foreign key (requester_id)
            references users (id)
            on delete cascade,
    constraint fk_request_event
        foreign key (event_id)
            references events (id)
            on delete cascade
);
