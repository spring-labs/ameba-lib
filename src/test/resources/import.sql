create schema if not exists SCHEMA_1;
drop table SCHEMA_1.test_entity if exists
drop sequence if exists SCHEMA_1.hibernate_sequence
create sequence SCHEMA_1.hibernate_sequence start with 1 increment by 1
create table SCHEMA_1.test_entity (id bigint not null, schema varchar(255), primary key (id))

create schema if not exists SCHEMA_2;
drop table SCHEMA_2.test_entity if exists
drop sequence if exists SCHEMA_2.hibernate_sequence
create sequence SCHEMA_2.hibernate_sequence start with 1 increment by 1
create table SCHEMA_2.test_entity (id bigint not null, schema varchar(255), primary key (id))
