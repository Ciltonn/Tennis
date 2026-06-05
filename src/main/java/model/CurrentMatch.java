package model;

import lombok.*;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter


public class CurrentMatch {
    private UUID matchId;
    private PlayerScore firstPlayerScore;
    private PlayerScore secondPlayerScore;
    private MatchState matchState;

    public void playerWinPoint(boolean isFirstPlayer) {
        if(isFirstPlayer){
            firstPlayerScore.winPoint();
        } else {
            secondPlayerScore.winPoint();
        }
    }
    public void playerWinGame(boolean isFirstPlayer) {
        if (isFirstPlayer) {
            firstPlayerScore.winGame();
        } else {
            secondPlayerScore.winGame();
        }
    }
    public void playerWinSet(boolean isFirstPlayer) {
        if(isFirstPlayer) {
            firstPlayerScore.winSet();
        } else {
            secondPlayerScore.winSet();
        }
    }

}
