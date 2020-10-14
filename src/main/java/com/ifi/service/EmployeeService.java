package com.ifi.service;


import com.ifi.model.EmployeeEntity;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface EmployeeService {
    List<EmployeeEntity> getAllEmployee();

    EmployeeEntity addNewEmployee(EmployeeEntity newEmployeeDTO);

    EmployeeEntity updateEmployee(EmployeeEntity existEmployeeDTO);

    boolean deleteEmployee(int id);
}
