package com.ifi.api;


import com.ifi.model.EmployeeEntity;

import java.util.List;

public interface EmployeeService {
    List<EmployeeEntity> getAllEmployee();

    EmployeeEntity addNewEmployee(EmployeeEntity newEmployeeDTO);

    EmployeeEntity updateEmployee(EmployeeEntity existEmployeeDTO);

    boolean deleteEmployee(int id);
}
