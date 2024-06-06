create schema if not exists T_SCHEMA_1;
drop table if EXISTS T_SCHEMA_1.test_entity;
drop sequence if exists T_SCHEMA_1.hibernate_sequence;
create sequence T_SCHEMA_1.hibernate_sequence start with 1 increment by 1;
drop sequence if exists T_SCHEMA_1.TEST_ENTITY_SEQ;
create sequence T_SCHEMA_1.TEST_ENTITY_SEQ start with 1 increment by 1;
create table T_SCHEMA_1.test_entity (id bigint not null, schema varchar(255), primary key (id));

create schema if not exists T_SCHEMA_2;
drop table if exists T_SCHEMA_2.test_entity;
drop sequence if exists T_SCHEMA_2.hibernate_sequence;
create sequence T_SCHEMA_2.hibernate_sequence start with 1 increment by 1;
drop sequence if exists T_SCHEMA_2.TEST_ENTITY_SEQ;
create sequence T_SCHEMA_2.TEST_ENTITY_SEQ start with 1 increment by 1;
create table T_SCHEMA_2.test_entity (id bigint not null, schema varchar(255), primary key (id));
