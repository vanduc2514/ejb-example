package com.ifi.dao;

import com.ifi.model.EmployeeEntity;

import javax.ejb.Local;
import java.util.List;

public interface EmployeeDAO {
    List<EmployeeEntity> getAllEmployee();

    EmployeeEntity addEmployee(EmployeeEntity newEmployee);

    EmployeeEntity updateEmployee(EmployeeEntity existEmployee);

    boolean deleteEmployee(int id);
}
