import dao.PlayerImpl;
import dto.CurrentMatch;
import entity.Player;
import org.junit.jupiter.api.BeforeEach;
import service.FinishedMatchesPersistenceService;
import service.MatchScoreCalculationService;
import service.NewMatchService;
import service.OngoingMatchService;

import java.util.UUID;

public class MatchScoreCalculationServiceTest {
    private MatchScoreCalculationService matchScoreCalculationService;
    private NewMatchService newMatchService;
    private TestOngoingMatchService ongoingMatchService;
    private TestPlayerImpl playerDao;
    private TestMatchImpl matchDao;
    private TestFinishedMatchesPersistenceService finishedMatchesPersistenceService;
    private UUID matchId;
    private CurrentMatch currentMatch;
    private Player player1;
    private Player player2;

static class TestOngoingMatchService extends OngoingMatchService {

}
static class TestPlayerImpl extends
    @BeforeEach
    void setUp() {


    }
}
