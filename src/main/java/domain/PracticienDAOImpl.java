package domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class PracticienDAOImpl implements Dao<Practicien> {

    private EntityManager entityManager;

    // standard constructors
    public PracticienDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Practicien> get(long id) {
        return Optional.ofNullable(entityManager.find(Practicien.class, id));
    }

    @Override
    public List<Practicien> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Practicien e");
        return query.getResultList();
    }

    @Override
    public Practicien save(Practicien Practicien) {
        executeInsideTransaction(entityManager -> entityManager.persist(Practicien));
        return Practicien;
    }


    @Override
    public void update(Practicien Practicien, Object[] params) {
        Practicien.setName(Objects.requireNonNull((String) params[0], "Name cannot be null"));
        Practicien.setAnciennete(Objects.requireNonNull((Date) params[1], "Anciennete cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(Practicien));
    }

    @Override
    public void delete(Practicien Practicien) {
        executeInsideTransaction(entityManager -> entityManager.remove(Practicien));
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
