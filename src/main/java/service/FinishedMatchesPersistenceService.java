package service;

import dao.MatchImpl;
import dao.PlayerImpl;
import model.CurrentMatch;
import entity.TennisMatch;
import entity.Player;
import exception.DatabaseOperationException;
import exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinishedMatchesPersistenceService {
    private final PlayerImpl playerDaoImpl;
    private final MatchImpl matchDaoImpl;

    public FinishedMatchesPersistenceService(PlayerImpl playerDaoImpl, MatchImpl matchDaoImpl) {
        this.playerDaoImpl = playerDaoImpl;
        this.matchDaoImpl = matchDaoImpl;
    }

    public void finishMatch(CurrentMatch currentMatch) {
        try {
            if (currentMatch == null) {
                throw new NotFoundException("Match not found");
            }
            if (!currentMatch.isFinished()) {
                throw new IllegalStateException("Cannot finish unfinished match");
            }
            Player firstPlayer = currentMatch.getFirstPlayer();
            if (firstPlayer == null) {
                throw new NotFoundException("Player not found");
            }
            Player savedFirstPlayer = playerDaoImpl.creatorOrSave(firstPlayer);
            Player secondPlayer = currentMatch.getSecondPlayer();
            if (secondPlayer == null) {
                throw new NotFoundException("Player not found");
            }
            Player savedSecondPlayer = playerDaoImpl.creatorOrSave(secondPlayer);
            Player winner = currentMatch.getWinner();
            if (winner == null) {
                throw new NotFoundException("Player not found");
            }
            TennisMatch match = new TennisMatch(savedFirstPlayer, savedSecondPlayer, winner);
            matchDaoImpl.save(match);

        } catch (Exception e) {
            log.error("Failed to finish match {}: {}", currentMatch.getMatchId(), e.getMessage(), e);
            throw new DatabaseOperationException("Failed to save finished match");
        }
    }
}
