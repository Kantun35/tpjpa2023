package DAO.DAOImpl;

import DAO.Dao;
import domain.Practicien;
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
    public Practicien save(Practicien practicien) {
        executeInsideTransaction(entityManager -> entityManager.persist(practicien));
        return practicien;
    }


    @Override
    public void update(Practicien practicien, Object[] params) {
        practicien.setName(Objects.requireNonNull((String) params[0], "Name cannot be null"));
        practicien.setAnciennete(Objects.requireNonNull((Date) params[1], "Anciennete cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(practicien));
    }

    @Override
    public void delete(Practicien practicien) {
        executeInsideTransaction(entityManager -> entityManager.remove(practicien));
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
