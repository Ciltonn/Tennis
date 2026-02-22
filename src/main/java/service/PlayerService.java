package service;

import dao.PlayerImpl;
import entity.Player;

public class PlayerService {
    private final PlayerImpl playerImpl;

    public PlayerService(PlayerImpl player) {
        this.playerImpl = player;
    }

    public Long convertNamePlayerToId(String playerName) {
        Player player = playerImpl.findByName(playerName).orElseThrow();
        return player.getId();
    }

}
