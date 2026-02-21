import dto.CurrentMatch;
import dto.MatchState;
import dto.TennisPoint;
import entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.MatchScoreCalculationService;
import service.OngoingMatchService;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class MatchScoreCalculationServiceTest {
    private MatchScoreCalculationService matchScoreCalculationService;
    private OngoingMatchService ongoingMatchService;
    private UUID matchId;
    private Player player1;
    private  Player player2;


    @BeforeEach
    void setUp() {
        player1 = new Player(1L, "Rafael Nadal");
        player2 = new Player(2L, "Pete Sampras");
        ongoingMatchService = new OngoingMatchService();
        matchScoreCalculationService = new MatchScoreCalculationService(ongoingMatchService);
        matchId = UUID.randomUUID();


    }
    @Test
    @DisplayName("При счете 40-0 выйгрыш очка дает гейм")
    void winGameAt40_0() {
        MatchState matchState = new MatchState(false, false, false, false, false);
        CurrentMatch currentMatch = CurrentMatch.builder()
                .matchId(matchId)
                .idPlayer1(1L)
                .idPlayer2(2L)
                .sets1(0)
                .games1(0)
                .points1(TennisPoint.FORTY)
                .sets2(0)
                .games2(0)
                .points2(TennisPoint.ZERO)
                .matchState(matchState)
                .build();
        ongoingMatchService.saveCurrentMatch(matchId, currentMatch);
        CurrentMatch match = matchScoreCalculationService.calculateScore(matchId, currentMatch.getIdPlayer1());
        assertEquals(TennisPoint.ZERO, match.getPoints1());
        assertEquals(TennisPoint.ZERO, match.getPoints2());
        assertEquals(1, match.getGames1());
        assertEquals(0, match.getGames2());
    }
    @Test
    @DisplayName("При 30-40 следующее очко первого игрока дает дьюс")
    void isDeuce() {
        MatchState matchState = new MatchState(false, false, false, false, false);
        CurrentMatch currentMatch = CurrentMatch.builder()
                .matchId(matchId)
                .idPlayer1(1L)
                .idPlayer2(2L)
                .sets1(0)
                .games1(0)
                .points1(TennisPoint.THIRTY)
                .sets2(0)
                .games2(0)
                .points2(TennisPoint.FORTY)
                .matchState(matchState)
                .build();
        ongoingMatchService.saveCurrentMatch(matchId, currentMatch);
        CurrentMatch match = matchScoreCalculationService.calculateScore(matchId, currentMatch.getIdPlayer1());
        assertEquals(TennisPoint.FORTY, match.getPoints1());
        assertEquals(TennisPoint.FORTY, match.getPoints2());
        assertTrue(match.getMatchState().isDeuce());

    }

    @Test
    @DisplayName("При дьюсе следующее очко дает преимущество")
    void notWinAfterDeuce() {
        MatchState matchState = new MatchState(false, false, true, false, false);
        CurrentMatch currentMatch = CurrentMatch.builder()
                .matchId(matchId)
                .idPlayer1(1L)
                .idPlayer2(2L)
                .sets1(0)
                .games1(0)
                .points1(TennisPoint.FORTY)
                .sets2(0)
                .games2(0)
                .points2(TennisPoint.FORTY)
                .matchState(matchState)
                .build();
        ongoingMatchService.saveCurrentMatch(matchId, currentMatch);
        CurrentMatch match = matchScoreCalculationService.calculateScore(matchId, currentMatch.getIdPlayer1());
        assertEquals(TennisPoint.ADVANTAGE, match.getPoints1());
        assertEquals(TennisPoint.FORTY, match.getPoints2());
        assertFalse(match.getMatchState().isDeuce());
        assertEquals(0, match.getGames1());
    }


    @Test
    @DisplayName("При счете 6-6 начинается тай-брейк")
    void processTieBreak() {
        MatchState matchState = new MatchState(false, false, false, false, false);
        CurrentMatch currentMatch = CurrentMatch.builder()
                .matchId(matchId)
                .idPlayer1(1L)
                .idPlayer2(2L)
                .sets1(0)
                .games1(5)
                .points1(TennisPoint.GAME)
                .sets2(0)
                .games2(6)
                .points2(TennisPoint.ZERO)
                .matchState(matchState)
                .build();
        ongoingMatchService.saveCurrentMatch(matchId, currentMatch);
        CurrentMatch match = matchScoreCalculationService.calculateScore(matchId, currentMatch.getIdPlayer1());
        assertTrue(match.getMatchState().isTieBreak());


    }
}

