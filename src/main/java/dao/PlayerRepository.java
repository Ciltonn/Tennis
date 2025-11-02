package dao;

import java.util.Optional;


public class PlayerRepository implements CrudDao{



    @Override
    public Optional findByName(String name) {

        return Optional.empty();
    }

    @Override
    public Object save(Object entity) {
        return null;
    }
}
