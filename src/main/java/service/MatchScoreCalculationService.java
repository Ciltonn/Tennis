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


        if (currentMatch.getMatchState().isTieBreakPlayer1() || currentMatch.getMatchState().isTieBreakPlayer2()) {
            processTieBreak(currentMatch, playerId);
        } else {
            processGame(currentMatch, playerId);
        }
        checkMatchFinished(currentMatch);
        return currentMatch;
    }

    private void processGame(CurrentMatch currentMatch, Long playerId) {
        scorePoints(playerId, currentMatch);
        checkMatchFineshed(currentMatch);
    }

    private void processTieBreak(CurrentMatch currentMatch, Long playerId) {
        if (currentMatch.getIdPlayer1().equals(playerId)) {
            currentMatch.setPoints1(currentMatch.getPoints1().getNextValuePoints());
        } else {
            currentMatch.setPoints2(currentMatch.getPoints2().getNextValuePoints());
        }
        checkTieBreakFinished(currentMatch);
    }

    private void scorePoints(Long playerId, CurrentMatch currentMatch) {
        if (currentMatch.getIdPlayer1().equals(playerId)) {
            TennisPoint currentPoint = currentMatch.getPoints1();
            if (currentMatch.getMatchState().isDeuce()) {
                handleDeuce(currentMatch, true);
            } else {
                currentMatch.setPoints1(currentPoint.getNextValuePoints());
            }
        } else {
            TennisPoint currentPoint = currentMatch.getPoints2();
            if (currentMatch.getMatchState().isDeuce()) {
                handleDeuce(currentMatch, false);
            } else {
                currentMatch.setPoints2(currentPoint.getNextValuePoints());
            }
        }
        checkDeuceState(currentMatch);
    }

    private void checkDeuceState(CurrentMatch currentMatch) {
        if (currentMatch.getPoints1() == TennisPoint.FORTY &&
                currentMatch.getPoints2() == TennisPoint.FORTY &&
                !currentMatch.getMatchState().isDeuce()) {
            MatchState state = currentMatch.getMatchState();
            state.setDeuce(true);
            currentMatch.setMatchState(state);
        }

    }

    private void handleDeuce(CurrentMatch currentMatch, boolean isPlayer1) {
        MatchState state = currentMatch.getMatchState();
        if (state.isPlayer1Advantage() && !isPlayer1) {
            state.setPlayer1Advantage(false);
            state.setDeuce(true);
        } else if (state.isPlayer2Advantage() && isPlayer1) {
            state.setPlayer2Advantage(false);
            state.setDeuce(true);
        } else if (state.isDeuce()) {
            if (isPlayer1) {
                state.setPlayer1Advantage(true);
                state.setDeuce(false);
            }
        } else if (isPlayer1 && state.isPlayer1Advantage()) {
            winGame(currentMatch, false);
            resetPoints(currentMatch);
        }
        currentMatch.setMatchState(state);
    }

    private void checkMatchFinished(CurrentMatch currentMatch) {
        if (!currentMatch.getMatchState().isDeuce() &&
                (!currentMatch.getMatchState().isPlayer1Advantage() &&
                        !currentMatch.getMatchState().isPlayer2Advantage())) {
            if (currentMatch.getPoints1() == TennisPoint.SETWINNER) {
                winGame(currentMatch, true);
                resetPoints(currentMatch);
            }
        }
    }

    private void winGame(CurrentMatch currentMatch, boolean isPlayer1) {
        if (isPlayer1) {
            currentMatch.setGames1(currentMatch.getGames1() + 1);
        } else {
            currentMatch.setGames2(currentMatch.getGames2() + 1);
        }
        checkSetFinished(currentMatch);

    }

    private void checkSetFinished(CurrentMatch currentMatch) {
        int games1 = currentMatch.getGames1();
        int games2 = currentMatch.getGames2();
        if ((games1 >= 6 || games2 >= 6) && Math.abs(games1 - games2) >= 2) {
            if (games1 > games2) {
                currentMatch.setSets1((currentMatch.getSets1() + 1));
            } else {
                currentMatch.setSets2(currentMatch.getSets2() + 1);
            }
            currentMatch.setGames1(0);
            currentMatch.setGames2(0);
            if (games1 == 6 && games2 == 6) {
                MatchState state = currentMatch.getMatchState();
                state.setTieBreakPlayer1(true);
                state.setTieBreakPlayer2(true);
                currentMatch.setMatchState(state);
            }
        }
    }

    private void checkTieBreakFinished(CurrentMatch currentMatch) {
        int points1 = currentMatch.getPoints1().ordinal();
        int points2 = currentMatch.getPoints2().ordinal();
        if((points1>=7 || points2>=7) && Math.abs(points1-points2)>=2) {
            if(points1>points2) {
                currentMatch.setSets1(currentMatch.getSets1()+1);
            } else  {
                currentMatch.setSets2(currentMatch.getSets2()+1);
            }
            MatchState state = currentMatch.getMatchState();
            state.setTieBreakPlayer1(false);
            state.setTieBreakPlayer2(false);
            currentMatch.setMatchState(state);
            resetPoints(currentMatch);
        }
    }
    private void checkMatchFineshed (CurrentMatch currentMatch) {
        if(currentMatch.getSets1()>=2 || currentMatch.getSets2()>=2) {
            MatchState state = currentMatch.getMatchState();
            state.setMatchOver(true);
            currentMatch.setMatchState(state);
        }
    }
    private void resetPoints (CurrentMatch currentMatch){
        currentMatch.setPoints1(TennisPoint.ZERO);
        currentMatch.setPoints2(TennisPoint.ZERO);
        MatchState state = currentMatch.getMatchState();
        state.setDeuce(false);
        state.setPlayer1Advantage(false);
        state.setPlayer2Advantage(false);
        currentMatch.setMatchState(state);
    }
}

