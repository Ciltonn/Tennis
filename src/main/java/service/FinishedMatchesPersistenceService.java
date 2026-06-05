package service;

import dao.MatchImpl;
import dao.PlayerImpl;
import model.CurrentMatch;
import entity.TennisMatch;
import entity.Player;
import exception.DatabaseOperationException;
import exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;


import java.util.UUID;
@Slf4j
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
            if(currentMatch==null) {
                throw new NotFoundException("Match not found");
            }
            if (!currentMatch.getMatchState().isMatchOver()) {
                throw new IllegalStateException("Cannot finish unfinished match");
            }
            Player firstPlayer = playerImpl.findById(currentMatch.getIdPlayer1()).
                    orElseThrow(()-> new NotFoundException("Player not found"));

            Player secondPlayer = playerImpl.findById(currentMatch.getIdPlayer2()).
                    orElseThrow(()-> new NotFoundException("Player not found"));
            Player winner = getWinner(currentMatch);
            TennisMatch match = new TennisMatch(firstPlayer, secondPlayer, winner);
            matchImpl.save(match);
            ongoingMatchService.deleteCurrentMatch(uuid);

        } catch (Exception e) {
            log.error("Failed to finish match {}: {}", uuid, e.getMessage(), e);
            throw new DatabaseOperationException("Failed to save finished match");
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
