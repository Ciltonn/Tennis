package service;

import dao.MatchImpl;
import dao.PlayerImpl;
import dto.CurrentMatch;
import entity.Match;


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

    public void finishMatch(UUID uuid, Long winnerId) {
        try {
            CurrentMatch currentMatch = ongoingMatchService.getCurrentMatch(uuid);
            if (currentMatch == null) {
                return;
            }
            Match match = new Match();
            match.setPlayer1(playerImpl.findById(currentMatch.getIdPlayer1()).orElseThrow());
            match.setPlayer2(playerImpl.findById(currentMatch.getIdPlayer2()).orElseThrow());
            match.setWinner(playerImpl.findById(winnerId).orElseThrow());
            matchImpl.save(match);
            ongoingMatchService.deleteCurrentMatch(uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
