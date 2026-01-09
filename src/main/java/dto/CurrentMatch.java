package dto;

import lombok.*;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class CurrentMatch {
    private Long idPlayer1;
    private Long idPlayer2;
    private UUID matchId;
    private int sets;
    private int games;
    private int points;


}
