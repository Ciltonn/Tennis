package dao;

import entity.Player;

import java.util.Optional;

public interface PlayerCrud extends CrudDao<Player, Long>{

    Optional <Player> findByName(String name);
}
