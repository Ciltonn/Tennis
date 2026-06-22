import model.CurrentMatch;
import model.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MatchScoreCalculationServiceTest {
    private CurrentMatch match;

    @BeforeEach
    void setUp() {
        match = new CurrentMatch("FirstPlayer", "SecondPlayer");
    }

    @Test
    @DisplayName("Первый игрок выигрывает одно очко при счете 0-0")
    void fromZeroToFifteen() {
        match.awardPointTo(1);
        assertEquals("15", match.getCurrentPointsFirstPlayer());
        assertEquals("0", match.getCurrentPointsSecondPlayer());
    }

    @Test
    @DisplayName("Первый игрок выигрывает одно очко при счете 15-0")
    void fromFifteenToThirty() {
        match.awardPointTo(1);
        match.awardPointTo(1);
        assertEquals("30", match.getCurrentPointsFirstPlayer());
        assertEquals("0", match.getCurrentPointsSecondPlayer());
    }

    @Test
    @DisplayName("Первый игрок выигрывает одно очко при счете 30-0")
    void fromThirtyToFourteen() {
        match.awardPointTo(1);
        match.awardPointTo(1);
        match.awardPointTo(1);
        assertEquals("40", match.getCurrentPointsFirstPlayer());
        assertEquals("0", match.getCurrentPointsSecondPlayer());
        assertFalse(match.getCurrentGame().isGameFinished());
    }

    @Test
    @DisplayName("Первый игрок выигрывает одно очко при счете 40-0")
    void fromFourteenToGame() {
        match.awardPointTo(1);
        match.awardPointTo(1);
        match.awardPointTo(1);
        match.awardPointTo(1);
        assertEquals("0", match.getCurrentPointsFirstPlayer());
        assertEquals("0", match.getCurrentPointsSecondPlayer());
        assertEquals(1, match.getCurrentGamesFirstPlayer());
    }

    @Test
    @DisplayName("При счете 40-40 наступает дьюс, гейм не завершен")
    void deuce() {
        match.awardPointTo(1);
        match.awardPointTo(1);
        match.awardPointTo(1);
        match.awardPointTo(2);
        match.awardPointTo(2);
        match.awardPointTo(2);
        assertEquals("40", match.getCurrentPointsFirstPlayer());
        assertEquals("40", match.getCurrentPointsSecondPlayer());
        assertFalse(match.getCurrentGame().isGameFinished());
    }
    @Test
    @DisplayName("После Дьюса (40-40) выигрыш очка дает преимущество (AD-40)")
    void fromDeuceToAdvantage() {
       for (int i = 0; i < 3; i++) {
            match.awardPointTo(1);
            match.awardPointTo(2);
        }
        match.awardPointTo(1);
        assertEquals("AD", match.getCurrentPointsFirstPlayer());
        assertEquals("40", match.getCurrentPointsSecondPlayer());
        assertFalse(match.getCurrentGame().isGameFinished());
    }
    @Test
    @DisplayName("Из Преимущества (AD-40) проигрыш очка возвращает в Дьюс (40-40)")
    void fromAdvantageBackToDeuce() {
       for (int i = 0; i < 3; i++) {
            match.awardPointTo(1);
            match.awardPointTo(2);
        }
        match.awardPointTo(1);
        assertEquals("AD", match.getCurrentPointsFirstPlayer());
        assertEquals("40", match.getCurrentPointsSecondPlayer());
        match.awardPointTo(2);
        assertEquals("40", match.getCurrentPointsFirstPlayer());
        assertEquals("40", match.getCurrentPointsSecondPlayer());
        assertFalse(match.getCurrentGame().isGameFinished());
    }
    @Test
    @DisplayName("При счете 6-6 в сете начинается тай-брейк")
    void startTieBreak() {
        for (int game = 0; game < 6; game++) {
            for (int point = 0; point < 4; point++) {
                match.awardPointTo(1);
            }
        }
        for (int game = 0; game < 6; game++) {
            for (int point = 0; point < 4; point++) {
                match.awardPointTo(2);
            }
        }
        assertEquals(6, match.getCurrentGamesFirstPlayer());
        assertEquals(6, match.getCurrentGamesSecondPlayer());
        Set currentSet = match.getSets().get(match.getSets().size()-1);
        assertTrue(currentSet.isTieBreak());
    }
}

