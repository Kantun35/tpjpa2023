package DAO;


import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(long id);

    List<T> getAll();

    T save(T t);

    void update(T t, Object[] params);

    void delete(T t);
}