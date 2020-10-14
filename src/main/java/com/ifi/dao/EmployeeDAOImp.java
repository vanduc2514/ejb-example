package com.ifi.dao;

import com.ifi.model.EmployeeEntity;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.*;
import java.util.List;

@Stateless
@Local(EmployeeDAO.class)
public class EmployeeDAOImp implements EmployeeDAO {
    @PersistenceContext
    EntityManager entityManager;

    @Resource
    UserTransaction userTransaction;

    @Override
    public List<EmployeeEntity> getAllEmployee() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeeEntity> criteriaQuery = criteriaBuilder.createQuery(EmployeeEntity.class);
        Root<EmployeeEntity> employeeEntityRoot = criteriaQuery.from(EmployeeEntity.class);
        criteriaQuery.select(employeeEntityRoot);
        TypedQuery<EmployeeEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Override
    public EmployeeEntity addEmployee(EmployeeEntity newEmployee) {
        if (newEmployee.getId() != null) {
            return null;
        }
        try {
            userTransaction.begin();
            entityManager.persist(newEmployee);
            entityManager.flush();
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            e.printStackTrace();
        }
        return newEmployee;
    }

    @Override
    public EmployeeEntity updateEmployee(EmployeeEntity existEmployee) {
        if (existEmployee.getId() == null) {
            return null;
        }
        try {
            userTransaction.begin();
            entityManager.merge(existEmployee);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            e.printStackTrace();
        }
        return existEmployee;
    }

    @Override
    public boolean deleteEmployee(int id) {
        EmployeeEntity employeeFound = entityManager.find(EmployeeEntity.class, id);
        return removeEntity(employeeFound);
    }

    private boolean removeEntity(EmployeeEntity employeeFound) {
        if (employeeFound == null) {
            return false;
        }
        try {
            userTransaction.begin();
            entityManager.remove(
                    entityManager.contains(employeeFound) ? employeeFound : entityManager.merge(employeeFound)
            );
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            e.printStackTrace();
        }
        return true;
    }
}
