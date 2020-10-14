package com.ifi.service;

import com.ifi.dao.EmployeeDAO;
import com.ifi.dto.EmployeeDTO;
import com.ifi.model.EmployeeEntity;
import com.ifi.util.Mapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class EmployeeServiceImp implements EmployeeService {
    @EJB
    EmployeeDAO employeeDAO;

    @EJB
    Mapper mapper;

    @Override
    public List<EmployeeDTO> getAllEmployee() {
        List<EmployeeEntity> employeeEntityList = employeeDAO.getAllEmployee();
        return employeeEntityList
                .stream()
                .map(employeeEntity -> mapper.map(employeeEntity, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO addNewEmployee(EmployeeDTO newEmployeeDTO) {
        EmployeeEntity employeeToSave = mapper.map(newEmployeeDTO, EmployeeEntity.class);
        EmployeeEntity employeeSaved = employeeDAO.addEmployee(employeeToSave);
        return mapper.map(employeeSaved, EmployeeDTO.class);
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO existEmployeeDTO) {
        EmployeeEntity employeeToUpdate = mapper.map(existEmployeeDTO, EmployeeEntity.class);
        EmployeeEntity employeeUpdated = employeeDAO.updateEmployee(employeeToUpdate);
        return mapper.map(employeeUpdated, EmployeeDTO.class);
    }

    @Override
    public boolean deleteEmployee(int id) {
        return employeeDAO.deleteEmployee(id);
    }
}
