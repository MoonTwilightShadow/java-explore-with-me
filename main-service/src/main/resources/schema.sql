drop table if exists users, categories, compilations, events, event_compilations, requests, ratings cascade;

create table if not exists users (
    id    integer generated always as identity not null,
    name  varchar(250)                         not null,
    email varchar(254)                         not null
);

alter table users
    add primary key (id);
alter table users
    add constraint user_uq_email unique (email);

create table if not exists categories (
    id   integer generated always as identity not null,
    name varchar(50)                          not null
);

alter table categories
    add primary key (id);
alter table categories
    add constraint category_uq_name unique (name);

create table if not exists compilations (
    id     integer generated always as identity not null,
    title  varchar(50)                          not null,
    pinned boolean
);

alter table compilations
    add primary key (id);

create table if not exists events (
    id                 integer generated always as identity not null,
    title              varchar(120)                         not null,
    annotation         varchar(2000)                        not null,
    description        varchar(7000)                        not null,
    event_date         timestamp without time zone          not null,
    created_on         timestamp without time zone,
    published_on       timestamp without time zone,
    paid               boolean,
    participant_limit  integer,
    request_moderation boolean,
    state              varchar(50),
    lat                float,
    lon                float,
    initiator_id       integer                              not null,
    category_id        integer                              not null,
    views              integer,
    like_count         integer                              not null,
    dislike_count      integer                              not null
);

alter table events
    add primary key (id);
alter table events
    add foreign key (initiator_id) references users (id) on delete cascade;
alter table events
    add foreign key (category_id) references categories (id) on delete cascade;

create table if not exists event_compilations (
    id             integer generated always as identity not null,
    compilation_id integer                              not null,
    event_id       integer                              not null
);

alter table event_compilations
    add primary key (id);
alter table event_compilations
    add foreign key (compilation_id) references compilations (id) on delete cascade;
alter table event_compilations
    add foreign key (event_id) references events (id) on delete cascade;

create table if not exists requests (
    id           integer generated always as identity not null,
    requester_id integer                              not null,
    event_id     integer                              not null,
    created      timestamp without time zone          not null,
    status       varchar(50)                          not null
);

alter table requests
    add primary key (id);
alter table requests
    add foreign key (requester_id) references users (id) on delete cascade;
alter table requests
    add foreign key (event_id) references events (id) on delete cascade;

create table if not exists ratings (
    id        integer generated always as identity not null,
    event_id  integer                              not null,
    user_id   integer                              not null,
    like_type varchar(10)
);

alter table ratings
    add primary key (id);
alter table ratings
    add foreign key (event_id) references events (id) on delete cascade;
alter table ratings
    add foreign key (user_id) references users (id) on delete cascade;
