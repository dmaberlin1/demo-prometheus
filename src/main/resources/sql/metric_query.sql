SELECT
    n.nspname AS schemaname,
    c.relname AS tablename,
    (SELECT count(*) FROM employees) AS row_count,
    pg_relation_size(c.oid) AS table_size_bytes
FROM
    pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
WHERE
    c.relkind IN ('r', 'm')  -- только таблицы и материализованные представления
  AND n.nspname NOT IN ('pg_catalog', 'information_schema')  -- исключаем системные схемы
  AND c.relname = 'employees'; -- фильтруем таблицу 'employees'

-- ORDER BY
--     pg_relation_size(c.oid) DESC;  -- сортировка по размеру таблицы


SELECT
    n.nspname AS schemaname,
    c.relname AS tablename,
    (SELECT count(*) FROM employees) AS row_count
FROM
    pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
WHERE
    c.relkind IN ('r', 'm')
  AND n.nspname NOT IN ('pg_catalog', 'information_schema')
  AND c.relname = 'employees';