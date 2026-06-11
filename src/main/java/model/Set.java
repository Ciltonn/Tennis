package model;

public class Set {
    private int firstPlayerGames;
    private int secondPlayersGames;
    private boolean isTieBreak;
    private TieBreakScore tieBreakScore;

    public Set(int firstPlayerGames, int secondPlayersGames) {
        this.firstPlayerGames = firstPlayerGames;
        this.secondPlayersGames = secondPlayersGames;
        this.isTieBreak = false;
        this.tieBreakScore = null;
    }

    public boolean addGame(int player) {
        if (isTieBreak) {
            boolean finished = tieBreakScore.addPoint(player);
            if (finished) {
                if (tieBreakScore.getWinner() == 1) {
                    firstPlayerGames++;
                } else {
                    secondPlayersGames++;
                }
                isTieBreak = false;
                tieBreakScore = null;
                return true;
            }
            return false;
        }
        if (player == 1) {
            firstPlayerGames++;
        } else {
            secondPlayersGames++;
        }
        updateTieBreak();
        return isSetFinished();
    }

    public boolean isSetFinished() {
        if (firstPlayerGames == 6 && secondPlayersGames == 6) {
            return false;
        }
        if (firstPlayerGames >= 6 && firstPlayerGames - secondPlayersGames >= 2) {
            return true;
        }
        if (secondPlayersGames >= 6 && secondPlayersGames - firstPlayerGames >= 2) {
            return true;
        }
        return false;
    }

    public int getWinnerSet() {
        if (!isSetFinished()) {
            return 0;
        }
        if (firstPlayerGames > secondPlayersGames) {
            return 1;
        } else return 2;
    }

    private void updateTieBreak() {
        if (firstPlayerGames == 6 && secondPlayersGames == 6 && !isTieBreak) {
            isTieBreak = true;
            tieBreakScore = new TieBreakScore();
        }
    }

    public int getFirstPlayerGames() {
        return firstPlayerGames;
    }

    public int getSecondPlayersGames() {
        return secondPlayersGames;
    }
}
