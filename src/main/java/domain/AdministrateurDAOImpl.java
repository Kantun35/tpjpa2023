package domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.hsqldb.lib.List;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class AdministrateurDAOImpl implements Dao<Administrateur> {

    private EntityManager entityManager;

    // standard constructors

    @Override
    public Optional<Administrateur> get(long id) {
        return Optional.ofNullable(entityManager.find(Administrateur.class, id));
    }

    @Override
    public List<Administrateur> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM employee e where type_employee = 'ADM'");
        return (List<Administrateur>) query.getResultList();
    }

    @Override
    public void save(Administrateur administrateur) {
        executeInsideTransaction(entityManager -> entityManager.persist(administrateur));
    }

    @Override
    public void update(Administrateur administrateur, String[] params) {
        administrateur.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
        administrateur.setMdpEnClaire(Objects.requireNonNull(params[0], "MdpEnClaire cannot be null"));
        executeInsideTransaction(entityManager -> entityManager.merge(administrateur));
    }

    @Override
    public void delete(Administrateur administrateur) {
        executeInsideTransaction(entityManager -> entityManager.remove(administrateur));
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
