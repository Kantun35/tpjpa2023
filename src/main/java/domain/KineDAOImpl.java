package domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class KineDAOImpl implements Dao<Kine> {

    private EntityManager entityManager;

    // standard constructors
    public KineDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Kine> get(long id) {
        return Optional.ofNullable(entityManager.find(Kine.class, id));
    }

    @Override
    public List<Kine> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Kine e");
        return query.getResultList();
    }

    @Override
    public Kine save(Kine Kine) {
        executeInsideTransaction(entityManager -> entityManager.persist(Kine));
        return Kine;
    }


    @Override
    public void update(Kine Kine, Object[] params) {
        Kine.setColonneCasseeEnDeux(Objects.requireNonNull((int) params[0], "ColonneCasseEnDeux cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(Kine));
    }

    @Override
    public void delete(Kine Kine) {
        executeInsideTransaction(entityManager -> entityManager.remove(Kine));
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
