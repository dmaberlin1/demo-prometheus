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


INSERT INTO employees (first_name, last_name, hire_date, salary)
VALUES ( 'John1', 'Doe1', '2023-01-01', 50000.00),
       ( 'Jane', 'Smith', '2023-01-02', 60000.00),
       ( 'Michael', 'Johnson', '2023-01-03', 55000.00),
       ( 'Emily', 'Williams', '2023-01-04', 52000.00),
       ( 'Daniel', 'Brown', '2023-01-05', 58000.00),
       ( 'Olivia', 'Jones', '2023-01-06', 53000.00),
       ( 'William', 'Garcia', '2023-01-07', 54000.00),
       ( 'Sophia', 'Martinez', '2023-01-08', 57000.00),
       ( 'Alexander', 'Davis', '2023-01-09', 56000.00),
       ( 'Isabella', 'Rodriguez', '2023-01-10', 59000.00);


DELETE FROM public.employees
WHERE id IN (
    SELECT id
    FROM public.employees
    ORDER BY id DESC
    LIMIT 200
);