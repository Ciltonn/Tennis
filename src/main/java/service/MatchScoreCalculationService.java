package service;

import dao.PlayerImpl;
import dto.CurrentMatch;
import dto.MatchState;
import dto.TennisPoint;
import entity.Player;

import java.util.UUID;

public class MatchScoreCalculationService {
    private OngoingMatchService ongoingMatchService;
    private PlayerImpl playerImpl;
    private FinishedMatchesPersistenceService finishedMatchesPersistenceService;

    public MatchScoreCalculationService(OngoingMatchService ongoingMatchService, PlayerImpl playerImpl) {
        this.ongoingMatchService = ongoingMatchService;
        this.playerImpl = playerImpl;
        this.finishedMatchesPersistenceService = finishedMatchesPersistenceService;
    }

    public CurrentMatch calculateScore(UUID uuid, String playerName) {
        CurrentMatch currentMatch = ongoingMatchService.getCurrentMatch(uuid);
        Player player = playerImpl.findByName(playerName).orElseThrow();
        Long playerId = player.getId();
        MatchState state = currentMatch.getMatchState();

        if(!state.isTieBreak()) {
            if (!state.isDeuce()) {
                if (!state.isPlayer1Advantage() || !state.isPlayer2Advantage()) {
                    countingGame(currentMatch, playerId);
                } else {
                    countingAdvantage(currentMatch, playerId);
                }
            } else {
                countingDeuce(currentMatch, playerId);
            }
        } else {
            countingTieBreak(currentMatch, playerId);
        }


        return currentMatch;
    }

    private void countingGame(CurrentMatch currentMatch, Long playerId) {
        if (playerId.equals(currentMatch.getIdPlayer1())) {
            TennisPoint tennisPoint = currentMatch.getPoints1();
            currentMatch.setPoints1(tennisPoint.getNextValuePoints());
        } else {
            TennisPoint tennisPoint = currentMatch.getPoints2();
            currentMatch.setPoints2(tennisPoint.getNextValuePoints());
        }
        isDeucePoints(currentMatch);
    }

    private void countingAdvantage(CurrentMatch currentMatch, Long playerId) {
        MatchState state = currentMatch.getMatchState();
        if (state.isPlayer1Advantage() && currentMatch.getIdPlayer1().equals(playerId)) {
            state.setPlayer1Advantage(false);
            currentMatch.setPoints1(TennisPoint.ZERO);
            currentMatch.setGames1(currentMatch.getGames1()+1);
        } else if(state.isPlayer2Advantage() && currentMatch.getIdPlayer2().equals(playerId)) {
            state.setPlayer2Advantage(false);
            currentMatch.setPoints2(TennisPoint.ZERO);
            currentMatch.setGames2(currentMatch.getGames1()+1);
        }
        isWinSet(currentMatch);
        isTieBreakGames(currentMatch, playerId);
    }

    private void countingDeuce(CurrentMatch currentMatch, Long playerId) {
        MatchState state = currentMatch.getMatchState();
        if (!state.isPlayer1Advantage() && !state.isPlayer2Advantage()) {
            if (playerId.equals(currentMatch.getIdPlayer1())) {
                currentMatch.setPoints1(TennisPoint.ADVANTAGE);
                currentMatch.setPoints2(TennisPoint.NOTADVANTAGE);
                state.setPlayer1Advantage(true);
                state.setDeuce(false);
            } else {
                currentMatch.setPoints2(TennisPoint.ADVANTAGE);
                currentMatch.setPoints1(TennisPoint.NOTADVANTAGE);
                state.setPlayer2Advantage(true);
                state.setDeuce(false);
            }

        }
    }

    private void isDeucePoints(CurrentMatch currentMatch) {
        if (currentMatch.getPoints1() == TennisPoint.FORTY && currentMatch.getPoints2() == TennisPoint.FORTY) {
            MatchState state = currentMatch.getMatchState();
            state.setDeuce(true);
        }
    }
    private void isTieBreakGames(CurrentMatch currentMatch, Long playerId) {
        MatchState state = currentMatch.getMatchState();
        if(currentMatch.getGames1()==6 && currentMatch.getGames2()==6) {
          state.setTieBreak(true);
            }
        }

    private void isWinSet(CurrentMatch currentMatch) {
        if(currentMatch.getGames1()==6 && (currentMatch.getGames1()-currentMatch.getGames2())==2) {
            currentMatch.setSets1(currentMatch.getSets1()+1);
            currentMatch.setGames1(0);
        } else if(currentMatch.getGames2()==6 && (currentMatch.getGames2()-currentMatch.getGames1())==2) {
            currentMatch.setSets2(currentMatch.getSets2()+1);
            currentMatch.setGames1(0);
        }
    }
    private void   countingTieBreak(CurrentMatch currentMatch, Long playerId) {
        currentMatch.setPoints1(TennisPoint.ZERO);
        currentMatch.setPoints2(TennisPoint.ZERO);
        currentMatch.setGames1(0);
        currentMatch.setGames2(0);
    }


}

