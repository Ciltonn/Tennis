package service;

import dao.PlayerImpl;
import dto.MatchState;
import dto.PlayerRequest;
import dto.CurrentMatch;
import dto.TennisPoint;
import entity.Player;

import java.util.UUID;

import static service.OngoingMatchService.currentMatches;

public class NewMatchService {
    private  final PlayerImpl playerImpl;

    public NewMatchService(PlayerImpl playerImpl) {
        this.playerImpl = playerImpl;
                    }

    public CurrentMatch createNewMatch (PlayerRequest player1, PlayerRequest player2) {
        Player playerOne = creatOrSavePlayer(player1);
        Player playerTwo = creatOrSavePlayer(player2);
        UUID matchId = UUID.randomUUID();
        CurrentMatch currentNewMatch = new CurrentMatch(matchId, playerOne.getId(), playerTwo.getId(),0,0, TennisPoint.ZERO, 0,0,TennisPoint.ZERO,new MatchState());
        currentMatches.put(matchId, currentNewMatch);
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
