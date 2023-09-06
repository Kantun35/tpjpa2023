package domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.hsqldb.lib.List;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class EmployeeDAOImpl implements Dao<Employee> {

    private EntityManager entityManager;

    // standard constructors

    @Override
    public Optional<Employee> get(long id) {
        return Optional.ofNullable(entityManager.find(Employee.class, id));
    }

    @Override
    public List<Employee> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM employee e");
        return (List<Employee>) query.getResultList();
    }

    @Override
    public void save(Employee employee) {
        executeInsideTransaction(entityManager -> entityManager.persist(employee));
    }

    @Override
    public void update(Employee employee, String[] params) {
        employee.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
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