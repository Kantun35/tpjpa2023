package domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@NamedQuery(name = "UserEntity.findByUserId", query = "SELECT e FROM Department e WHERE e.name = :name")
public class DepartmentDAOImpl implements Dao<Department> {

    private EntityManager entityManager;

    // standard constructors
    public DepartmentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Department> get(long id) {
        return Optional.ofNullable(entityManager.find(Department.class, id));
    }

    @Override
    public List<Department> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Department e");
        return query.getResultList();
    }

    @Override
    public Department save(Department department) {
        executeInsideTransaction(entityManager -> entityManager.persist(department));
        return department;
    }

    @Override
    public void update(Department department, Object[] params) {
        department.setName(Objects.requireNonNull((String) params[0], "Name cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(department));
    }

    @Override
    public void delete(Department department) {
        executeInsideTransaction(entityManager -> entityManager.remove(department));
    }

    public Optional<Department> findDPTByName(String name) {
        Query query = entityManager.createNamedQuery("find_dpt_by_name",Department.class);
        query.setParameter("name",name);
        return Optional.ofNullable((Department) query.getSingleResult());
    }

    private void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            action.accept(entityManager);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }
}
