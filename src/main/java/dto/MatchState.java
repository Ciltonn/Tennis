package dto;

import lombok.*;

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
