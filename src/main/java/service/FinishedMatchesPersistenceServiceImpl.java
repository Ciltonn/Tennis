package service;

import dao.MatchDaoImpl;
import dao.PlayerDaoImpl;
import model.CurrentMatch;
import entity.TennisMatch;
import entity.Player;
import exception.DatabaseOperationException;
import exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

@Slf4j
public class FinishedMatchesPersistenceServiceImpl implements FinishedMatchesPersistenceService {
    private final PlayerDaoImpl playerDaoImpl;
    private final MatchDaoImpl matchDaoImpl;

    public FinishedMatchesPersistenceServiceImpl(PlayerDaoImpl playerDaoImpl, MatchDaoImpl matchDaoImpl) {
        this.playerDaoImpl = playerDaoImpl;
        this.matchDaoImpl = matchDaoImpl;
    }
@Override
    public void finishMatch(CurrentMatch currentMatch) {
        if (currentMatch == null) {
            throw new NotFoundException("Match not found");
        }
        if (!currentMatch.isFinished()) {
            throw new IllegalStateException("Cannot finish unfinished match");
        }
        String firstPlayer = currentMatch.getFirstPlayer();
        if (firstPlayer == null) {
            throw new NotFoundException("Player not found");
        }
        String secondPlayer = currentMatch.getSecondPlayer();
        if (secondPlayer == null) {
            throw new NotFoundException("Player not found");
        }
        String winner = currentMatch.getWinner();
        if (winner == null) {
            throw new NotFoundException("Player not found");
        }
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            try {
                Player savedFirstPlayer = playerDaoImpl.creatorOrSave(firstPlayer);
                Player savedSecondPlayer = playerDaoImpl.creatorOrSave(secondPlayer);
                Player savedWinner;
                if (winner.equals(savedFirstPlayer.getName())) {
                    savedWinner = savedFirstPlayer;
                } else {
                    savedWinner = savedSecondPlayer;
                }
                TennisMatch match = new TennisMatch(savedFirstPlayer, savedSecondPlayer, savedWinner);
                matchDaoImpl.save(match);
            } catch (Exception e) {
                log.error("Failed to save match data: {}", e.getMessage(), e);
                throw new DatabaseOperationException("Failed to save finished match: " + e.getMessage(), e);
            }
        } catch (HibernateException e) {
            log.error("Failed to finish match {}: {}", currentMatch.getMatchId(), e.getMessage(), e);
            throw new DatabaseOperationException("Failed to save finished match");
        }
    }
}
