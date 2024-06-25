CREATE TABLE IF NOT EXISTS public.employees (
                                                id         BIGSERIAL PRIMARY KEY,
                                                first_name VARCHAR(50),
                                                last_name  VARCHAR(50),
                                                hire_date  TIMESTAMP,
                                                salary     NUMERIC(10, 2)
);

alter table public.employees
    owner to postgres;

create index if not exists idx_employees_last_name
    on public.employees (last_name);


INSERT INTO employees (id, first_name, last_name, hire_date, salary)
VALUES (1, 'John', 'Doe', '2023-01-01', 50000.00),
       (2, 'Jane', 'Smith', '2023-01-02', 60000.00),
       (3, 'Michael', 'Johnson', '2023-01-03', 55000.00),
       (4, 'Emily', 'Williams', '2023-01-04', 52000.00),
       (5, 'Daniel', 'Brown', '2023-01-05', 58000.00),
       (6, 'Olivia', 'Jones', '2023-01-06', 53000.00),
       (7, 'William', 'Garcia', '2023-01-07', 54000.00),
       (8, 'Sophia', 'Martinez', '2023-01-08', 57000.00),
       (9, 'Alexander', 'Davis', '2023-01-09', 56000.00),
       (10, 'Isabella', 'Rodriguez', '2023-01-10', 59000.00);
