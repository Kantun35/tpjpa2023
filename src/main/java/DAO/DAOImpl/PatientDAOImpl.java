package DAO.DAOImpl;

import DAO.Dao;
import domain.Patient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

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
    public Patient save(Patient patient) {
        executeInsideTransaction(entityManager -> entityManager.persist(patient));
        return patient;
    }


    @Override
    public void update(Patient patient, Object[] params) {
        patient.setNumSecSoc(Objects.requireNonNull((String) params[0], "NumSecSoc cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(patient));
    }

    @Override
    public void delete(Patient patient) {
        executeInsideTransaction(entityManager -> entityManager.remove(patient));
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
