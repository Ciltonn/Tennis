
import dao.MatchImpl;
import dao.PlayerImpl;
import dto.CurrentMatch;
import dto.MatchState;
import dto.TennisPoint;
import entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FinishedMatchesPersistenceService;
import service.MatchScoreCalculationService;
import service.OngoingMatchService;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class MatchScoreCalculationServiceTest {
    private MatchScoreCalculationService matchScoreCalculationService;
    private PlayerImpl playerImpl;
    private MatchImpl matchImpl;
    private FinishedMatchesPersistenceService finishedMatchesPersistenceService;
    private OngoingMatchService ongoingMatchService;
    private UUID matchId;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player(1L, "Rafael Nadal");
        player2 = new Player(2L, "Pete Sampras");
        playerImpl = new PlayerImpl();
        matchImpl = new MatchImpl();
        ongoingMatchService = new OngoingMatchService();
        finishedMatchesPersistenceService = new FinishedMatchesPersistenceService(ongoingMatchService, playerImpl, matchImpl);
        matchScoreCalculationService = new MatchScoreCalculationService(playerImpl, finishedMatchesPersistenceService, ongoingMatchService);
        matchId = UUID.randomUUID();


    }

    @Test
    void notWinAfterDeuce() {
        MatchState matchState = new MatchState(false, false, false, false, false);
        CurrentMatch currentMatch = CurrentMatch.builder()
                .matchId(UUID.randomUUID())
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
        CurrentMatch match = matchScoreCalculationService.saveMatch(matchId, "Rafael Nadal");
        assertEquals(TennisPoint.ADVANTAGE, match.getPoints1());
        assertEquals(TennisPoint.FORTY, match.getPoints2());
        assertTrue(match.getMatchState().isPlayer1Advantage());
        assertEquals(0, match.getGames1());
    }

    @Test
    void winGame() {
        MatchState matchState = new MatchState(false, false, false, false, false);
        CurrentMatch currentMatch = CurrentMatch.builder()
                .matchId(UUID.randomUUID())
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
        CurrentMatch match = matchScoreCalculationService.saveMatch(matchId, "Rafael Nadal");
        assertEquals(TennisPoint.GAME, match.getPoints1());
        assertEquals(TennisPoint.ZERO, match.getPoints2());
        assertTrue(match.getGames1() == 1);
        assertFalse(match.getGames1() == 0);
    }

    @Test
    void processTieBreak() {
        MatchState matchState = new MatchState(false, false, false, false, false);
        CurrentMatch currentMatch = CurrentMatch.builder()
                .matchId(UUID.randomUUID())
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
        CurrentMatch match = matchScoreCalculationService.saveMatch(matchId, "Rafael Nadal");
        assertTrue(match.getMatchState().isTieBreak());
        assertEquals(1, match.getGames1());

    }
}

