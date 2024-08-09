create schema if not exists task_management_sys_schema;
SET search_path = task_management_sys_schema, public;
--
-- create table task_management_sys_schema.comments
-- (
--     id         bigint generated by default as identity,
--     created_at timestamp(6) not null,
--     text       varchar(255) not null,
--     task_id    bigint       not null,
--     user_id    bigint       not null,
--     primary key (id)
-- );
--
-- create table task_management_sys_schema.tasks
-- (
--     id          bigint generated by default as identity,
--     created_at  timestamp(6) not null,
--     description varchar(255) not null,
--     priority    varchar(255) not null check (priority in ('LOW', 'MEDIUM', 'HIGH')),
--     status      varchar(255) not null check (status in ('PENDING', 'IN_PROGRESS', 'COMPLETED')),
--     title       varchar(255) not null,
--     updated_at  timestamp(6) not null,
--     assignee_id bigint,
--     author_id   bigint       not null,
--     primary key (id)
-- );
--
-- create table task_management_sys_schema.users
-- (
--     id         bigint generated by default as identity,
--     email      varchar(255) not null,
--     first_name varchar(255) not null,
--     last_name  varchar(255) not null,
--     password   varchar(255) not null,
--     primary key (id)
-- );
--
-- create table task_management_sys_schema.users_roles_enum
-- (
--     user_id bigint not null,
--     roles   varchar(255) check (roles in ('ROLE_USER', 'ROLE_ADMIN', 'ROLE_TASK_AUTHOR'))
-- );