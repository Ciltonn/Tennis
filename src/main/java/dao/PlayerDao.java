package dao;

import entity.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDao {

    Optional <Player> findByName(String name);
    Optional <Player> findById (Long id);
    Player creatorOrSave (Player player);
    List<Player> findAll(int offset, int limit);
    long count();
}
