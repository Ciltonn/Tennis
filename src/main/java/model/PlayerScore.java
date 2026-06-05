package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PlayerScore {
    private Long playerId;
    private int sets;
    private int games;
    private TennisPoint points;

    public void winPoint() {
        points.next();
    }
    public void winGame() {
        games++;
    }
    public void winSet(){
        sets++;
    }
    private void resetPoints() {
        this.points = TennisPoint.ZERO;
    }
    public void resetGames() {
        this.games=0;
    }
   }
