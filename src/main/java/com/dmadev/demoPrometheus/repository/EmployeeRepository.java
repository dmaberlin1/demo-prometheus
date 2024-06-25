package com.dmadev.demoPrometheus.repository;

import com.dmadev.demoPrometheus.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
