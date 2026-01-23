package dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao <T, ID>{

  Optional<T> findById(ID id);

  T save(T entity);

  void delete(ID id);

  Optional <T> update(T entity);

  List<T> findAll();

   boolean existsById(ID id);

}
