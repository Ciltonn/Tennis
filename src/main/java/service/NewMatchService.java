package service;

import dao.PlayerImpl;
import model.CurrentMatch;
import model.Point;
import entity.Player;

import java.util.UUID;

import static service.OngoingMatchService.currentMatches;

public class NewMatchService {
    private final PlayerImpl playerImpl;

    public NewMatchService(PlayerImpl playerImpl) {
        this.playerImpl = playerImpl;
    }

    public CurrentMatch createNewMatch(PlayerRequest player1, PlayerRequest player2) {
        Player playerOne = createOrSavePlayer(player1);
        Player playerTwo = createOrSavePlayer(player2);
        UUID matchId = UUID.randomUUID();
        CurrentMatch currentNewMatch = new CurrentMatch(matchId, playerOne.getId(), playerTwo.getId(), 0, 0, Point.ZERO, 0, 0, Point.ZERO, new MatchState());
        currentMatches.put(matchId, currentNewMatch);
        return currentNewMatch;
    }

    public Player createOrSavePlayer(PlayerRequest playerRequest) {
        Player player = playerImpl.findByName(playerRequest.getName().trim()).orElse(null);
        if (player == null) {
            Player newPlayer=new Player(playerRequest.getName());
            player = playerImpl.save(newPlayer);
        }
        return player;
    }
}
