create table if not exists public.employees
(
    id         bigint not null
        primary key,
    first_name varchar(50),
    last_name  varchar(50),
    hire_date  timestamp,
    salary     numeric(10, 2)
);

alter table public.employees
    owner to postgres;

create index if not exists idx_employees_last_name
    on public.employees (last_name);
