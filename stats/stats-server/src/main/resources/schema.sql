drop table if exists stats cascade;

create table stats (
                       id integer primary key generated by default as identity not null,
                       app varchar(256) not null,
                       uri varchar(256) not null,
                       ip varchar(20) not null,
                       time_stat timestamp with time zone not null
);

