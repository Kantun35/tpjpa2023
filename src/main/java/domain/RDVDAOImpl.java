package domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jdk.jshell.execution.Util;

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

    public RDV prendreUnRDV(Utilisateur user, Patient patient, Practicien practicien, Date date) {
        Objects.requireNonNull(date, "Date cannot be null");
        Objects.requireNonNull(patient, "Patient cannot be null");
        Objects.requireNonNull(practicien, "Practicien cannot be null");

        RDV rdv = null;
        if(user instanceof Patient) {
            rdv = new RDV(date,patient,practicien,"en attente de validation");
            save(rdv);
        }
        return rdv;
    }

    public RDV validerUnRDV(Utilisateur user,RDV rdv) {
        Objects.requireNonNull(rdv, "rdv cannot be null");
        Objects.requireNonNull(user, "user cannot be null");

        if(user instanceof Practicien && isEtatValid(rdv)) {
            rdv.setEtat("Validé");
            executeInsideTransaction(entityManager -> entityManager.merge(rdv));
        }
        return rdv;
    }

    public RDV refuserUnRDV(Utilisateur user, RDV rdv) {
        Objects.requireNonNull(rdv, "rdv cannot be null");
        Objects.requireNonNull(user, "user cannot be null");

        if(user instanceof Practicien && isEtatValid(rdv)) {
            rdv.setEtat("Refusé");
            executeInsideTransaction(entityManager -> entityManager.merge(rdv));
        }
        return rdv;
    }

    public RDV annulerUnRDV(Utilisateur user, RDV rdv) {
        Objects.requireNonNull(rdv, "rdv cannot be null");
        Objects.requireNonNull(user, "user cannot be null");

        if(user instanceof Practicien && isEtatValid(rdv)) {
            rdv.setEtat("Annulé");
            executeInsideTransaction(entityManager -> entityManager.merge(rdv));
        }
        return rdv;
    }

    public RDV reporterUnRDV(Utilisateur user, RDV rdv, Date date) {
        Objects.requireNonNull(rdv, "rdv cannot be null");
        Objects.requireNonNull(user, "user cannot be null");
        Objects.requireNonNull(date, "date cannot be null");

        if(user instanceof Practicien && isEtatValid(rdv)) {
            rdv.setDate(date);
            rdv.setEtat("Reporté");
        }
    }

    private Boolean isEtatValid(RDV rdv) {
        if (Objects.equals(rdv.getEtat(), "Annulé") || Objects.equals(rdv.getEtat(), "Refusé")) {
            return false;
        } else {
            return true;
        }
    }
}
