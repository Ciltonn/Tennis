package service;

import model.CurrentMatch;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchServiceImpl implements OngoingMatchService {
    private static final Map<UUID, CurrentMatch> currentMatches = new ConcurrentHashMap<>();

    @Override
    public CurrentMatch getCurrentMatch(UUID matchId) {
        return currentMatches.get(matchId);
    }

    @Override
    public void saveCurrentMatch(UUID match_id, CurrentMatch currentMatch) {
        currentMatches.put(match_id, currentMatch);
    }
}
