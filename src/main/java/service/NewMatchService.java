package service;

import dao.PlayerImpl;
import dto.PlayerRequest;
import dto.CurrentMatch;
import entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class NewMatchService {
    private  final PlayerImpl playerImpl;
    private static final HashMap<UUID, CurrentMatch> currentMatch = new HashMap<>();

    public NewMatchService(PlayerImpl playerImpl) {
        this.playerImpl = playerImpl;
    }

    public CurrentMatch creatNewMatch (PlayerRequest player1, PlayerRequest player2) {
        Player playerOne = creatOrSavePlayer(player1);
        Player playerTwo = creatOrSavePlayer(player2);
        UUID matchId = UUID.randomUUID();
        CurrentMatch currentNewMatch = new CurrentMatch(playerOne.getId(), playerTwo.getId(), matchId, 0,0,0);
        currentMatch.put(matchId, currentNewMatch);
        return currentNewMatch;
    }

    public Player creatOrSavePlayer(PlayerRequest playerRequest) {
        Player player = playerImpl.findByName(playerRequest.getName()).orElse(null);
        if (player == null) {
            player = new Player();
            player.setName(playerRequest.getName());
            player = playerImpl.save(player);
        }
        return player;
    }




}
