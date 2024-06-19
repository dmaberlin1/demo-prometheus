package com.dmadev.prometheus.service;


import com.dmadev.prometheus.entity.Employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface EmployeeService {


    public List<Object[]>executeAndLogQueryResults();

    public List<Employee> getAllEmployees();

}
