package domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class RDVDAOImpl implements Dao<RDV> {

    private EntityManager entityManager;

    // standard constructors

    public RDVDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<RDV> get(long id) {
        return Optional.ofNullable(entityManager.find(RDV.class, id));
    }

    @Override
    public List<RDV> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM RDV e");
        return query.getResultList();
    }

    @Override
    public RDV save(RDV rdv) {
        executeInsideTransaction(entityManager -> entityManager.persist(rdv));
        return rdv;
    }


    @Override
    public void update(RDV rdv, Object[] params) {
        rdv.setDate(Objects.requireNonNull((Date) params[0], "Date cannot be null"));
        rdv.setEtat(Objects.requireNonNull((String) params[1], "Etat cannot be null"));
        rdv.setPatient(Objects.requireNonNull((Patient) params[2], "Patient cannot be null"));
        rdv.setPracticien(Objects.requireNonNull((Practicien) params[3], "Practicien cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(rdv));
    }

    @Override
    public void delete(RDV rdv) {
        executeInsideTransaction(entityManager -> entityManager.remove(rdv));
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
