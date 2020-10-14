package com.ifi.service;

import com.ifi.dao.EmployeeDAO;
import com.ifi.model.EmployeeEntity;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class EmployeeServiceImp implements EmployeeService {
    @EJB
    EmployeeDAO employeeDAO;

    @Override
    public List<EmployeeEntity> getAllEmployee() {
        return employeeDAO.getAllEmployee();
    }

    @Override
    public EmployeeEntity addNewEmployee(EmployeeEntity newEmployeeDTO) {
        return employeeDAO.addEmployee(newEmployeeDTO);
    }

    @Override
    public EmployeeEntity updateEmployee(EmployeeEntity existEmployeeDTO) {
        return employeeDAO.updateEmployee(existEmployeeDTO);
    }

    @Override
    public boolean deleteEmployee(int id) {
        return employeeDAO.deleteEmployee(id);
    }
}
