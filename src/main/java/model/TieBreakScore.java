package model;

public class TieBreakScore {
    private int firstPlayerScore;
    private int secondPlayerScore;

    public TieBreakScore() {
        this.firstPlayerScore = 0;
        this.secondPlayerScore = 0;
    }
     public boolean addPoint(int player) {
        if (player == 1) firstPlayerScore++;
        else secondPlayerScore++;
        return isFinished();
    }

    public boolean isFinished() {
        if (firstPlayerScore >= 7 && firstPlayerScore - secondPlayerScore >= 2) return true;
        if (secondPlayerScore >= 7 && secondPlayerScore - firstPlayerScore >= 2) return true;
        return false;
    }
    public int getWinner() {
        if (!isFinished()) return 0;
        return firstPlayerScore > secondPlayerScore ? 1 : 2;
    }

}
