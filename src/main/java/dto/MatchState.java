package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchState {
    boolean matchOver = false;
    boolean tieBreak = false;
    boolean deuce = false;
    boolean player1Advantage = false;
    boolean player2Advantage = false;
}
