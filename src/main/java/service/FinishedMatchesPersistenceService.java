package service;

import dao.MatchImpl;
import dao.PlayerImpl;
import dto.CurrentMatch;
import dto.FinishedMatch;
import entity.Player;

import java.util.UUID;

public class FinishedMatchesPersistenceService {

    private MatchScoreCalculationService MatchScoreCalculationService;
    private OngoingMatchService ongoingMatchService;
    private PlayerImpl playerImpl;
    private MatchImpl matchImpl;

    public FinishedMatchesPersistenceService(MatchScoreCalculationService matchScoreCalculationService, OngoingMatchService ongoingMatchService, PlayerImpl playerImpl, MatchImpl matchImpl) {
        MatchScoreCalculationService = matchScoreCalculationService;
        this.ongoingMatchService = ongoingMatchService;
        this.playerImpl = playerImpl;
        this.matchImpl = matchImpl;
    }

    public void finishMatch (UUID uuid, Long idPlayer) {
        CurrentMatch currentMatch = ongoingMatchService.getCurrentMatch(uuid);
        Player player1 = playerImpl.findById(currentMatch.getIdPlayer1()).orElseThrow();
        Player player2 = playerImpl.findById(currentMatch.getIdPlayer2()).orElseThrow();
        Player winner = playerImpl.findById(idPlayer).orElseThrow();

        FinishedMatch finishedMatch = new FinishedMatch(player1, player2, winner);
        matchImpl.save(finishedMatch);

       ongoingMatchService.deleteCurrentMatch(uuid);
    }
}
