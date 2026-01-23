package dto;

import lombok.*;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CurrentMatch {
    private UUID matchId;
    private Long idPlayer1;
    private Long idPlayer2;
    private int sets1;
    private int games1;
    private TennisPoint points1;
    private int sets2;
    private int games2;
    private TennisPoint points2;
    private MatchState matchState;

}
