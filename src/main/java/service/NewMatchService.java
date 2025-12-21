package service;

import dao.PlayerImpl;
import dto.NewMatch;
import dto.PlayerRequest;
import entity.Match;
import entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class NewMatchService {

    private final PlayerImpl playerImpl = new PlayerImpl();
    HashMap<UUID, NewMatch> currentMatch = new HashMap<>();



    public Player creatOrSavePlayer(PlayerRequest playerRequest) {

        Player player = playerImpl.findByName(playerRequest.getName()).orElse(null);
        if (player == null) {
            player = new Player();
            player.setName(playerRequest.getName());
            player = playerImpl.save(player);
        }
        return player;
    }

    public NewMatch creatMatch (Player player1, Player player2) {
        Long idPlayer1 = player1.getId();
        Long idPlayer2 = player2.getId();
        NewMatch newMatch = new NewMatch(idPlayer1, idPlayer2, 0,0,0);
        return newMatch;
    }

    public void addCurrentMatch ( NewMatch newMatch) {
        currentMatch.put((UUID.randomUUID()), newMatch);
    }
}
