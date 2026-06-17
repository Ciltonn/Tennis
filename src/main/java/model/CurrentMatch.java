package model;


import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter


public class CurrentMatch {
    private static final int SETS_TO_WIN = 2;
    private UUID matchId;
    private String firstPlayer;
    private String secondPlayer;
    private List<Set> sets;
    private Game currentGame;
    private boolean isFinished;
    private String winner;

    public CurrentMatch(String firstPlayer, String secondPlayer) {
        this.matchId = UUID.randomUUID();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.sets = new ArrayList<>();
        this.currentGame = new Game();
        this.isFinished = false;
        this.winner = null;
    }

    public void awardPointTo(int playerNumber) {
        if (isFinished) {
            return;
        }
        if (sets.isEmpty()) {
            sets.add(new Set(0,0));
        }
        currentGame.addPoint(playerNumber);
        if (currentGame.isGameFinished()) {
            int gameWinner = currentGame.getWinnerGame();
            Set currentSet = sets.get(sets.size() - 1);
            currentSet.addGame(gameWinner);
            currentGame = new Game();
            if (currentSet.isSetFinished()) {
                int firstWon = 0;
                int secondWon = 0;
                for (Set set : sets) {
                    if (set.isSetFinished()) {
                        if (set.getWinnerSet() == 1) {
                            firstWon++;
                        } else if (set.getWinnerSet() == 2) {
                            secondWon++;
                        }
                    }
                }
                if (firstWon == SETS_TO_WIN) {
                    isFinished = true;
                    winner = firstPlayer;
                } else if (secondWon == SETS_TO_WIN) {
                    isFinished = true;
                    winner = secondPlayer;
                } else {
                    sets.add(new Set(0,0));
                }
            }
        }
    }
    public boolean isMatchFinished() {
        return isFinished;
    }


    public int getSetsWonByFirstPlayer() {
        int count = 0;
        for (Set set : sets) {
            if (set.isSetFinished() && set.getWinnerSet() == 1) {
                count++;
            }
        }
        return count;
    }

    public int getSetsWonBySecondPlayer() {
        int count = 0;
        for (Set set : sets) {
            if (set.isSetFinished() && set.getWinnerSet() == 2) {
                count++;
            }
        }
        return count;
    }

    public int getCurrentGamesFirstPlayer() {
        if (sets.isEmpty()) return 0;
        return sets.get(sets.size() - 1).getFirstPlayerGames();
    }

    public int getCurrentGamesSecondPlayer() {
        if (sets.isEmpty()) return 0;
        return sets.get(sets.size() - 1).getSecondPlayersGames();
    }

    public String getCurrentPointsFirstPlayer() {
        return currentGame.getPointDisplay(1);
    }

    public String getCurrentPointsSecondPlayer() {
        return currentGame.getPointDisplay(2);
    }
}


