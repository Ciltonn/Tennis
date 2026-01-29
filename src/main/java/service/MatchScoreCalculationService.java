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

        if (!state.isTieBreak()) {
            if (!state.isDeuce()) {
                if (!state.isPlayer1Advantage() && !state.isPlayer2Advantage()) {
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
        isGameWin(currentMatch, playerId);
        isWinSet(currentMatch);
    }

    private void isGameWin(CurrentMatch currentMatch, Long playerId) {
        MatchState state = currentMatch.getMatchState();
        if (!state.isDeuce() && !state.isPlayer1Advantage() && !state.isPlayer2Advantage()) {
            if (playerId.equals(currentMatch.getIdPlayer1())) {
                TennisPoint tennisPoint1 = currentMatch.getGames1();
                if (currentMatch.getPoints1() == TennisPoint.GAME) {
                    currentMatch.setPoints1(TennisPoint.ZERO);
                    currentMatch.setGames1(tennisPoint1.getNextValueGame());
                    currentMatch.setPoints2(TennisPoint.ZERO);
                }
            } else if (playerId.equals(currentMatch.getIdPlayer2())) {
            if (currentMatch.getPoints2() == TennisPoint.GAME) {
                    TennisPoint tennisPoint2 = currentMatch.getGames2();
                    currentMatch.setPoints2(TennisPoint.ZERO);
                    currentMatch.setGames2(tennisPoint2.getNextValueGame());
                currentMatch.setPoints1(TennisPoint.ZERO);
                }
            }
        }
    }

    private void countingAdvantage(CurrentMatch currentMatch, Long playerId) {
        MatchState state = currentMatch.getMatchState();

        if (state.isPlayer1Advantage() && currentMatch.getIdPlayer1().equals(playerId)) {
            TennisPoint tennisPoint1 = currentMatch.getGames1();
            state.setPlayer1Advantage(false);
            currentMatch.setPoints1(TennisPoint.ZERO);
            currentMatch.setPoints2(TennisPoint.ZERO);
            currentMatch.setGames1(tennisPoint1.getNextValueGame());
        } else if (state.isPlayer2Advantage() && currentMatch.getIdPlayer2().equals(playerId)) {
            TennisPoint tennisPoint2 = currentMatch.getGames2();
            state.setPlayer2Advantage(false);
            currentMatch.setPoints2(TennisPoint.ZERO);
            currentMatch.setPoints1(TennisPoint.ZERO);
            currentMatch.setGames2(tennisPoint2.getNextValueGame());
        } else {
            state.setPlayer1Advantage(false);
            state.setPlayer2Advantage(false);
            state.setDeuce(true);
            currentMatch.setPoints2(TennisPoint.FORTY);
            currentMatch.setPoints1(TennisPoint.FORTY);
        }
        currentMatch.setMatchState(state);
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
            currentMatch.setMatchState(state);
        }
    }

    private void isTieBreakGames(CurrentMatch currentMatch, Long playerId) {
        MatchState state = currentMatch.getMatchState();
        if (currentMatch.getGames1() == TennisPoint.SIXGAME && currentMatch.getGames2() == TennisPoint.SIXGAME) {
            state.setTieBreak(true);
            currentMatch.setMatchState(state);
            currentMatch.setGames1(TennisPoint.ZEROGAME);
            currentMatch.setGames2(TennisPoint.ZEROGAME);
        }
    }

    private void isWinSet(CurrentMatch currentMatch) {
        int scoreGamePlayer1 = currentMatch.getGames1().getValue();
        int scoreGamePlayer2 = currentMatch.getGames2().getValue();
        if (currentMatch.getGames1() == TennisPoint.SIXGAME && (scoreGamePlayer1-scoreGamePlayer2>=2)) {
            currentMatch.setSets1(currentMatch.getSets1() + 1);
            currentMatch.setGames1(TennisPoint.ZEROGAME);
            currentMatch.setGames2(TennisPoint.ZEROGAME);
        } else if (currentMatch.getGames2() == TennisPoint.SIXGAME && (scoreGamePlayer2-scoreGamePlayer1>=2)) {
            currentMatch.setSets2(currentMatch.getSets2() + 1);
            currentMatch.setGames2(TennisPoint.ZEROGAME);
            currentMatch.setGames1(TennisPoint.ZEROGAME);
        }
    }

    private void countingTieBreak(CurrentMatch currentMatch, Long playerId) {
        TennisPoint tennisPoint1 = currentMatch.getGames1();
        if (playerId.equals(currentMatch.getIdPlayer1())) {
            currentMatch.setGames1(tennisPoint1.getNextValueGame());
        } else if(playerId.equals(currentMatch.getIdPlayer2())) {
            TennisPoint tennisPoint2 = currentMatch.getGames2();
            currentMatch.setGames2(tennisPoint2.getNextValueGame());
        }
        }
    }


