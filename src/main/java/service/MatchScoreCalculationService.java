package service;

import model.CurrentMatch;
import model.Point;

import java.util.UUID;


public class MatchScoreCalculationService {

//    private static final int MIN_POINT_DIFFERENCE = 2;
//    private static final int MIN_GAMES_TO_WIN_TIEBREAK = 7;
//    private static final int MIN_GAMES_TO_WIN_SET = 6;
//    private static final int SETS_TO_WIN_MATCH = 2;
    private final OngoingMatchService ongoingMatchService;

    public MatchScoreCalculationService(OngoingMatchService ongoingMatchService) {
        this.ongoingMatchService = ongoingMatchService;
    }

    public CurrentMatch calculateScore(UUID uuid, Long playerId) {
        CurrentMatch currentMatch = ongoingMatchService.getCurrentMatch(uuid);
        if (!currentMatch.getMatchState().equals(MatchState.MATCH_OVER)) {
            if (!currentMatch.getMatchState().equals(MatchState.TIE_BREAK)) {
                if (currentMatch.getMatchState().equals(MatchState.DEUCE)) {
                    countingDeuce(currentMatch, playerId);
                } else if (currentMatch.getMatchState().equals(MatchState.ADVANTAGE)) {
                    countingAdvantage(currentMatch, playerId);
                } else {
                    countingGame(currentMatch, playerId);
                }
            } else {
                countingTieBreak(currentMatch, playerId);
            }
            checkMatchOver(currentMatch);
        }

        return currentMatch;
    }

    private void countingGame(CurrentMatch currentMatch, Long playerId) {
        if (playerId.equals(currentMatch.getIdPlayer1())) {
            Point tennisPoint = currentMatch.getPoints1();
            if(!(currentMatch.getPoints1()== Point.FORTY &&
            currentMatch.getPoints2()== Point.FORTY)) {
            currentMatch.setPoints1(tennisPoint.next());
            }
        } else {
            Point tennisPoint = currentMatch.getPoints2();
            if(!(currentMatch.getPoints2()== Point.FORTY &&
                    currentMatch.getPoints1()== Point.FORTY)) {
                currentMatch.setPoints2(tennisPoint.next());
            }

        }
        checkDeuce(currentMatch);
        checkGameWin(currentMatch, playerId);
    }

    private void checkGameWin(CurrentMatch currentMatch, Long playerId) {
        MatchState state = currentMatch.getMatchState();
        if (!state.isDeuce() && !state.isPlayer1Advantage() && !state.isPlayer2Advantage()) {
            if (playerId.equals(currentMatch.getIdPlayer1())) {
                if (currentMatch.getPoints1().equals(Point.ADVANTAGE)) {
                    currentMatch.setGames1(currentMatch.getGames1() + 1);
                    resetPoints(currentMatch);
                    checkSetWin(currentMatch);
                }
            } else if (playerId.equals(currentMatch.getIdPlayer2())) {
                if (currentMatch.getPoints2().equals(Point.ADVANTAGE)) {
                    currentMatch.setGames2(currentMatch.getGames2() + 1);
                    resetPoints(currentMatch);
                    checkSetWin(currentMatch);
                }
            }
        }
    }

    private void countingAdvantage(CurrentMatch currentMatch, Long playerId) {
        MatchState state = currentMatch.getMatchState();
        if (state.isPlayer1Advantage() && currentMatch.getIdPlayer1().equals(playerId)) {
            state.setPlayer1Advantage(false);
            resetPoints(currentMatch);
            currentMatch.setGames1(currentMatch.getGames1() + 1);
            checkSetWin(currentMatch);
        } else if (state.isPlayer2Advantage() && currentMatch.getIdPlayer2().equals(playerId)) {
            state.setPlayer2Advantage(false);
            resetPoints(currentMatch);
            currentMatch.setGames2(currentMatch.getGames2() + 1);
            checkSetWin(currentMatch);
        } else {
            state.setPlayer1Advantage(false);
            state.setPlayer2Advantage(false);
            state.setDeuce(true);
            currentMatch.setPoints2(Point.FORTY);
            currentMatch.setPoints1(Point.FORTY);
        }
        currentMatch.setMatchState(state);
    }

    private void countingDeuce(CurrentMatch currentMatch, Long playerId) {
        MatchState state = currentMatch.getMatchState();
        if (!state.isPlayer1Advantage() && !state.isPlayer2Advantage()) {
            if (playerId.equals(currentMatch.getIdPlayer1())) {
                currentMatch.setPoints1(Point.ADVANTAGE);
                state.setPlayer1Advantage(true);
                state.setDeuce(false);
            } else {
                currentMatch.setPoints2(Point.ADVANTAGE);
                state.setPlayer2Advantage(true);
                state.setDeuce(false);
            }
        }
        currentMatch.setMatchState(state);
    }

    private void checkDeuce(CurrentMatch currentMatch) {
        if (currentMatch.getPoints1() == Point.FORTY &&
                currentMatch.getPoints2() == Point.FORTY) {
            MatchState state = currentMatch.getMatchState();
            state.setDeuce(true);
            currentMatch.setMatchState(state);
        }
    }

    private void checkSetWin(CurrentMatch currentMatch) {
        if (currentMatch.getGames1() >= MIN_GAMES_TO_WIN_SET &&
                ((currentMatch.getGames1() - currentMatch.getGames2()) >= MIN_POINT_DIFFERENCE)) {
            currentMatch.setSets1(currentMatch.getSets1() + 1);
            resetGames(currentMatch);
            resetMatchState(currentMatch);
            checkMatchOver(currentMatch);
        } else if (currentMatch.getGames2() >= MIN_GAMES_TO_WIN_SET &&
                ((currentMatch.getGames2() - currentMatch.getGames1()) >= MIN_POINT_DIFFERENCE)) {
            currentMatch.setSets2(currentMatch.getSets2() + 1);
            resetGames(currentMatch);
            resetMatchState(currentMatch);
            checkMatchOver(currentMatch);
        }
        if (currentMatch.getGames1() == MIN_GAMES_TO_WIN_SET &&
                currentMatch.getGames2() == MIN_GAMES_TO_WIN_SET) {
            MatchState state = currentMatch.getMatchState();
            state.setTieBreak(true);
            resetPoints(currentMatch);
            resetMatchState(currentMatch);
        }
    }

    private void resetMatchState(CurrentMatch currentMatch) {
        MatchState state = currentMatch.getMatchState();
        state.setDeuce(false);
        state.setPlayer1Advantage(false);
        state.setPlayer2Advantage(false);
    }

    private void countingTieBreak(CurrentMatch currentMatch, Long playerId) {
        if (playerId.equals(currentMatch.getIdPlayer1())) {
            currentMatch.setGames1(currentMatch.getGames1() + 1);
        }
        if (playerId.equals(currentMatch.getIdPlayer2())) {
            currentMatch.setGames2(currentMatch.getGames2() + 1);
        }
        checkTieBreakOver(currentMatch);
    }

    private void checkTieBreakOver(CurrentMatch currentMatch) {
        if ((currentMatch.getGames1() >= MIN_GAMES_TO_WIN_TIEBREAK ||
                currentMatch.getGames2() >= MIN_GAMES_TO_WIN_TIEBREAK) &&
                Math.abs(currentMatch.getGames1() - currentMatch.getGames2()) >= MIN_POINT_DIFFERENCE) {
            if (currentMatch.getGames1() > currentMatch.getGames2()) {
                currentMatch.setSets1(currentMatch.getSets1() + 1);
            } else {
                currentMatch.setSets2(currentMatch.getSets2() + 1);
            }
            currentMatch.getMatchState().setTieBreak(false);
            resetGamesAndPoints(currentMatch);
            resetMatchState(currentMatch);
            checkMatchOver(currentMatch);
        }
    }

    private void checkMatchOver(CurrentMatch currentMatch) {
        MatchState state = currentMatch.getMatchState();
        if (currentMatch.getSets1() >= SETS_TO_WIN_MATCH
                || currentMatch.getSets2() >= SETS_TO_WIN_MATCH) {
            state.setMatchOver(true);
        }
    }

    private void resetGamesAndPoints(CurrentMatch currentMatch) {
        currentMatch.setGames1(0);
        currentMatch.setGames2(0);
        currentMatch.setPoints1(Point.ZERO);
        currentMatch.setPoints2(Point.ZERO);
    }

    private void resetGames(CurrentMatch currentMatch) {
        currentMatch.setGames1(0);
        currentMatch.setGames2(0);
    }

    private void resetPoints(CurrentMatch currentMatch) {
        currentMatch.setPoints1(Point.ZERO);
        currentMatch.setPoints2(Point.ZERO);
    }
}




