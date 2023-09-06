package domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.hsqldb.lib.List;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class DepartmentDAOImpl implements Dao<Department> {

    private EntityManager entityManager;

    // standard constructors

    @Override
    public Optional<Department> get(long id) {
        return Optional.ofNullable(entityManager.find(Department.class, id));
    }

    @Override
    public List<Department> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Department e");
        return (List<Department>) query.getResultList();
    }

    @Override
    public void save(Department department) {
        executeInsideTransaction(entityManager -> entityManager.persist(department));
    }

    @Override
    public void update(Department department, String[] params) {
        department.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(department));
    }

    @Override
    public void delete(Department department) {
        executeInsideTransaction(entityManager -> entityManager.remove(department));
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
