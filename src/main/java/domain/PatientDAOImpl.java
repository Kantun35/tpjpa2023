package domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class PatientDAOImpl implements Dao<Patient> {

    private EntityManager entityManager;

    // standard constructors
    public PatientDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Patient> get(long id) {
        return Optional.ofNullable(entityManager.find(Patient.class, id));
    }

    @Override
    public List<Patient> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Patient e");
        return query.getResultList();
    }

    @Override
    public Patient save(Patient Patient) {
        executeInsideTransaction(entityManager -> entityManager.persist(Patient));
        return Patient;
    }


    @Override
    public void update(Patient Patient, Object[] params) {
        Patient.setNumSecSoc(Objects.requireNonNull((String) params[0], "NumSecSoc cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(Patient));
    }

    @Override
    public void delete(Patient Patient) {
        executeInsideTransaction(entityManager -> entityManager.remove(Patient));
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
