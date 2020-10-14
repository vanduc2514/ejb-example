package com.ifi.service;

import com.ifi.dto.EmployeeDTO;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface EmployeeService {
    List<EmployeeDTO> getAllEmployee();

    EmployeeDTO addNewEmployee(EmployeeDTO newEmployeeDTO);

    EmployeeDTO updateEmployee(EmployeeDTO existEmployeeDTO);

    boolean deleteEmployee(int id);
}
