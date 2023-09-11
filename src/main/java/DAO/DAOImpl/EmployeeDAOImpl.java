package DAO.DAOImpl;

import DAO.Dao;
import domain.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class EmployeeDAOImpl implements Dao<Employee> {

    private EntityManager entityManager;

    // standard constructors
    public EmployeeDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Employee> get(long id) {
        return Optional.ofNullable(entityManager.find(Employee.class, id));
    }

    @Override
    public List<Employee> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Employee e");
        return (List<Employee>) query.getResultList();
    }

    @Override
    public Employee save(Employee employee) {
        executeInsideTransaction(entityManager -> entityManager.persist(employee));
        return employee;
    }

    @Override
    public void update(Employee employee, Object[] params) {
        employee.setName(Objects.requireNonNull((String) params[0], "Name cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(employee));
    }

    @Override
    public void delete(Employee employee) {
        executeInsideTransaction(entityManager -> entityManager.remove(employee));
    }

    private void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            action.accept(entityManager);
            tx.commit();
        }
        catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }
}