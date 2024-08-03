
create table if not exists user_roles
(
    id bigserial not null ,
    role varchar(255) not null,
    primary key (id, role)
)