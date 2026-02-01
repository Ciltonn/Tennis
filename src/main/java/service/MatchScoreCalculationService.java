package service;

import dao.PlayerImpl;
import dto.CurrentMatch;
import dto.MatchState;
import dto.TennisPoint;
import entity.Player;

import java.util.UUID;

public class MatchScoreCalculationService {

    private static final int MIN_POINT_DIFFERENCE = 2;
    private static final int MIN_GAMES_TO_WIN_TIEBREAK = 7;
    private static final int MIN_GAMES_TO_WIN_SET = 6;
    private static final int SETS_TO_WIN_MATCH = 2;
    private OngoingMatchService ongoingMatchService;
    private PlayerImpl playerImpl;
    private FinishedMatchesPersistenceService finishedMatchesPersistenceService;

    public MatchScoreCalculationService(OngoingMatchService ongoingMatchService, PlayerImpl playerImpl, FinishedMatchesPersistenceService finishedMatchesPersistenceService) {
        this.ongoingMatchService = ongoingMatchService;
        this.playerImpl = playerImpl;
        this.finishedMatchesPersistenceService = finishedMatchesPersistenceService;
    }

    public CurrentMatch calculateScore(UUID uuid, String playerName) {
        CurrentMatch currentMatch = ongoingMatchService.getCurrentMatch(uuid);
        Player player = playerImpl.findByName(playerName).orElseThrow();
        Long playerId = player.getId();
        MatchState state = currentMatch.getMatchState();

        if (!state.isMatchOver()) {
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
            checkMatchOver(currentMatch);
        } else {
            Long winner = getWinner(currentMatch);
            finishedMatchesPersistenceService.finishMatch(uuid, winner);
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
                if (currentMatch.getPoints1().equals(TennisPoint.GAME)) {
                    currentMatch.setPoints1(TennisPoint.ZERO);
                    currentMatch.setGames1(tennisPoint1.getNextValueGame());
                    currentMatch.setPoints2(TennisPoint.ZERO);
                }
            } else if (playerId.equals(currentMatch.getIdPlayer2())) {
                if (currentMatch.getPoints2().equals(TennisPoint.GAME)) {
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

    private void isWinSet(CurrentMatch currentMatch) {
        int scoreGamePlayer1 = currentMatch.getGames1().getValue();
        int scoreGamePlayer2 = currentMatch.getGames2().getValue();
        if ((scoreGamePlayer1 >= MIN_GAMES_TO_WIN_SET) && ((scoreGamePlayer1 - scoreGamePlayer2) >= MIN_POINT_DIFFERENCE)) {
            currentMatch.setSets1(currentMatch.getSets1() + 1);
            currentMatch.setGames1(TennisPoint.ZEROGAME);
            currentMatch.setGames2(TennisPoint.ZEROGAME);
        } else if ((scoreGamePlayer2 >= MIN_GAMES_TO_WIN_SET) && ((scoreGamePlayer2 - scoreGamePlayer1) >= MIN_POINT_DIFFERENCE)) {
            currentMatch.setSets2(currentMatch.getSets2() + 1);
            currentMatch.setGames2(TennisPoint.ZEROGAME);
            currentMatch.setGames1(TennisPoint.ZEROGAME);
        }
        if (scoreGamePlayer1 == MIN_GAMES_TO_WIN_SET && scoreGamePlayer2 == MIN_GAMES_TO_WIN_SET) {
            MatchState state = currentMatch.getMatchState();
            state.setTieBreak(true);
            currentMatch.setGames1(TennisPoint.ZEROGAME);
            currentMatch.setGames2(TennisPoint.ZEROGAME);
            currentMatch.setPoints1(TennisPoint.ZERO);
            currentMatch.setPoints2(TennisPoint.ZERO);
        }
    }

    private void countingTieBreak(CurrentMatch currentMatch, Long playerId) {

        if (playerId.equals(currentMatch.getIdPlayer1())) {
            TennisPoint tennisPoint1 = currentMatch.getGames1();
            currentMatch.setGames1(tennisPoint1.getNextValueGame());
        }

        if (playerId.equals(currentMatch.getIdPlayer2())) {
            TennisPoint tennisPoint2 = currentMatch.getGames2();
            currentMatch.setGames2(tennisPoint2.getNextValueGame());
        }
        checkTieBreakOver(currentMatch);
    }

    private void checkTieBreakOver(CurrentMatch currentMatch) {
        int scoreGamePlayer1 = currentMatch.getGames1().getValue();
        int scoreGamePlayer2 = currentMatch.getGames2().getValue();
        if ((currentMatch.getGames1().equals(TennisPoint.SEVENGAME) && (scoreGamePlayer1 - scoreGamePlayer2 >= MIN_POINT_DIFFERENCE)) ||
                (currentMatch.getGames2().equals(TennisPoint.SEVENGAME) && (scoreGamePlayer2 - scoreGamePlayer1 >= MIN_POINT_DIFFERENCE))) {
            if (scoreGamePlayer1 > scoreGamePlayer2) {
                currentMatch.setSets1(currentMatch.getSets1() + 1);
            } else {
                currentMatch.setSets2(currentMatch.getSets2() + 1);
            }
        }
    }

    private void checkMatchOver(CurrentMatch currentMatch) {
        MatchState state = currentMatch.getMatchState();
        int setsPlayer1 = currentMatch.getSets1();
        int setsPlayer2 = currentMatch.getSets2();
        if (setsPlayer1 >= SETS_TO_WIN_MATCH || setsPlayer2 >= SETS_TO_WIN_MATCH) {
            state.setMatchOver(true);
            currentMatch.setMatchState(state);
        }
    }

    private Long getWinner(CurrentMatch currentMatch) {
        int setsPlayer1 = currentMatch.getSets1();
        int setsPlayer2 = currentMatch.getSets2();

        if (currentMatch.getMatchState().isMatchOver()) {
            if (setsPlayer1 > setsPlayer2) {
                return currentMatch.getIdPlayer1();
            } else if (setsPlayer2 > setsPlayer1) {
                return currentMatch.getIdPlayer2();
            }
        }
        throw new IllegalStateException("Match is't finished");
    }
}



