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

//    @Test
//    @DisplayName("При дьюсе следующее очко дает преимущество")
//    void notWinAfterDeuce() {
//        MatchState matchState = new MatchState(false, false, false, false, false);
//        CurrentMatch currentMatch = CurrentMatch.builder()
//                .matchId(matchId)
//                .idPlayer1(1L)
//                .idPlayer2(2L)
//                .sets1(0)
//                .games1(0)
//                .points1(TennisPoint.FORTY)
//                .sets2(0)
//                .games2(0)
//                .points2(TennisPoint.FORTY)
//                .matchState(matchState)
//                .build();
//        ongoingMatchService.saveCurrentMatch(matchId, currentMatch);
//        CurrentMatch match = matchScoreCalculationService.calculateScore(matchId, currentMatch.getIdPlayer1());
//        assertEquals(TennisPoint.ADVANTAGE, match.getPoints1());
//        assertEquals(TennisPoint.FORTY, match.getPoints2());
//        assertFalse(match.getMatchState().isDeuce());
//        assertEquals(0, match.getGames1());
//    }
@Test
@DisplayName("При дьюсе следующее очко дает преимущество")
void notWinAfterDeuce() {
    // Given
    System.out.println("=== Starting notWinAfterDeuce test ===");

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
            .points2(TennisPoint.FORTY)
            .matchState(matchState)
            .build();

    System.out.println("Initial state - Points: " +
            currentMatch.getPoints1() + "-" + currentMatch.getPoints2());
    System.out.println("Deuce flag: " + matchState.isDeuce());

    ongoingMatchService.saveCurrentMatch(matchId, currentMatch);

    // When
    System.out.println("Calling calculateScore for player 1");
    CurrentMatch match = matchScoreCalculationService.calculateScore(matchId, currentMatch.getIdPlayer1());

    // Then
    System.out.println("Final state - Points: " +
            match.getPoints1() + "-" + match.getPoints2());
    System.out.println("Player1 advantage: " + match.getMatchState().isPlayer1Advantage());
    System.out.println("Deuce flag: " + match.getMatchState().isDeuce());

    assertEquals(TennisPoint.ADVANTAGE, match.getPoints1(),
            "Player 1 should have advantage after winning point from deuce");
    assertEquals(TennisPoint.FORTY, match.getPoints2(),
            "Player 2 should still have 40");
    assertFalse(match.getMatchState().isDeuce(),
            "Deuce flag should be false");
    assertEquals(0, match.getGames1(),
            "Games should not increase");
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
    @DisplayName("При счете 6-6 начинается тай-брейк")
    void processTieBreak() {
        MatchState matchState = new MatchState(false, false, false, false, false);
        CurrentMatch currentMatch = CurrentMatch.builder()
                .matchId(matchId)
                .idPlayer1(1L)
                .idPlayer2(2L)
                .sets1(0)
                .games1(6)
                .points1(TennisPoint.ZERO)
                .sets2(0)
                .games2(6)
                .points2(TennisPoint.ZERO)
                .matchState(matchState)
                .build();
        ongoingMatchService.saveCurrentMatch(matchId, currentMatch);
        CurrentMatch match = matchScoreCalculationService.calculateScore(matchId, currentMatch.getIdPlayer1());
        assertTrue(match.getMatchState().isTieBreak());
        assertEquals(7, match.getGames1());
        assertEquals(6, match.getGames2());

    }
}

