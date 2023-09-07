package domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class UtilisateurDAOImpl implements Dao<Utilisateur> {

    private EntityManager entityManager;

    // standard constructors
    public UtilisateurDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Utilisateur> get(long id) {
        return Optional.ofNullable(entityManager.find(Utilisateur.class, id));
    }

    @Override
    public List<Utilisateur> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM Utilisateur e");
        return query.getResultList();
    }

    @Override
    public Utilisateur save(Utilisateur Utilisateur) {
        executeInsideTransaction(entityManager -> entityManager.persist(Utilisateur));
        return Utilisateur;
    }


    @Override
    public void update(Utilisateur Utilisateur, Object[] params) {
        Utilisateur.setName(Objects.requireNonNull((String) params[0], "NumSecSoc cannot be null"));
        Utilisateur.setTel(Objects.requireNonNull((String) params[1], "NumSecSoc cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(Utilisateur));
    }

    @Override
    public void delete(Utilisateur Utilisateur) {
        executeInsideTransaction(entityManager -> entityManager.remove(Utilisateur));
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
