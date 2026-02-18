package service;

import dao.MatchImpl;
import dao.PlayerImpl;
import dto.CurrentMatch;
import entity.Match;
import entity.Player;
import exception.NotFoundException;


import java.util.UUID;

public class FinishedMatchesPersistenceService {

    private final OngoingMatchService ongoingMatchService;
    private final PlayerImpl playerImpl;
    private final MatchImpl matchImpl;

    public FinishedMatchesPersistenceService(OngoingMatchService ongoingMatchService, PlayerImpl playerImpl, MatchImpl matchImpl) {
        this.ongoingMatchService = ongoingMatchService;
        this.playerImpl = playerImpl;
        this.matchImpl = matchImpl;
    }

    public void finishMatch(UUID uuid) {
        try {
            CurrentMatch currentMatch = ongoingMatchService.getCurrentMatch(uuid);
            Match match = new Match();
            match.setPlayer1(playerImpl.findById(currentMatch.getIdPlayer1()).orElseThrow());
            match.setPlayer2(playerImpl.findById(currentMatch.getIdPlayer2()).orElseThrow());
            match.setWinner(getWinner(currentMatch));
            matchImpl.save(match);
            ongoingMatchService.deleteCurrentMatch(uuid);

        } catch (Exception e) {
            throw new NotFoundException("Match not found");
        }
    }

    private Player getWinner(CurrentMatch currentMatch) {
        Player playerWinner;
        int setsPlayer1 = currentMatch.getSets1();
        int setsPlayer2 = currentMatch.getSets2();
        if (currentMatch.getMatchState().isMatchOver()) {
            if (setsPlayer1 > setsPlayer2) {
                playerWinner = playerImpl.findById(currentMatch.getIdPlayer1()).orElseThrow();
                return playerWinner;
            } else if (setsPlayer2 > setsPlayer1) {
                playerWinner = playerImpl.findById(currentMatch.getIdPlayer2()).orElseThrow();
                return playerWinner;
            }
        }
        throw new IllegalStateException("Match is't finished");
    }
}
