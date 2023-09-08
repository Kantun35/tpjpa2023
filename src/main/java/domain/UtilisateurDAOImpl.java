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
    public Utilisateur save(Utilisateur utilisateur) {
        executeInsideTransaction(entityManager -> entityManager.persist(utilisateur));
        return utilisateur;
    }


    @Override
    public void update(Utilisateur utilisateur, Object[] params) {
        utilisateur.setName(Objects.requireNonNull((String) params[0], "Name cannot be null"));
        utilisateur.setTel(Objects.requireNonNull((String) params[1], "Tel cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(utilisateur));
    }

    @Override
    public void delete(Utilisateur utilisateur) {
        executeInsideTransaction(entityManager -> entityManager.remove(utilisateur));
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
