package DAO.DAOImpl;

import DAO.Dao;
import domain.Chirurgien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ChirurgienDAOImpl implements Dao<Chirurgien> {

    private EntityManager entityManager;

    // standard constructors
    public ChirurgienDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Chirurgien> get(long id) {
        return Optional.ofNullable(entityManager.find(Chirurgien.class, id));
    }

    @Override
    public List<Chirurgien> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Chirurgien e");
        return query.getResultList();
    }

    @Override
    public Chirurgien save(Chirurgien chirurgien) {
        executeInsideTransaction(entityManager -> entityManager.persist(chirurgien));
        return chirurgien;
    }


    @Override
    public void update(Chirurgien chirurgien, Object[] params) {
        chirurgien.setOperationRatee(Objects.requireNonNull((int) params[0], "OperationRatee cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(chirurgien));
    }

    @Override
    public void delete(Chirurgien chirurgien) {
        executeInsideTransaction(entityManager -> entityManager.remove(chirurgien));
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
