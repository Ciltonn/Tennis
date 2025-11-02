package dao;

import java.util.Optional;

public interface CrudDao <T, Id>{

    Optional<T> findByName (String name);

    T save(T entity);

}
