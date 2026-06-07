package model;

public class Set {
    private int firstPlayerGames;
    private int secondPlayersGames;
    private boolean isTieBreak;

    public Set() {
        this.firstPlayerGames = 0;
        this.secondPlayersGames = 0;
        this.isTieBreak = false;
    }

    public void addGame(int player) {
        if (player == 1) {
            firstPlayerGames++;
        } else secondPlayersGames++;
        updateTieBreak();
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
        if (firstPlayerGames == 6 && secondPlayersGames == 6) {
            isTieBreak = true;
        }
    }

    public int getFirstPlayerGames() {
        return firstPlayerGames;
    }

    public int getSecondPlayersGames() {
        return secondPlayersGames;
    }
}
