package service;

import model.CurrentMatch;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchService {
    private static final Map<UUID, CurrentMatch> currentMatches = new ConcurrentHashMap<>();

    public CurrentMatch getCurrentMatch(UUID matchId) {
        return currentMatches.get(matchId);
    }
    public void saveCurrentMatch(UUID match_id, CurrentMatch currentMatch) {
        currentMatches.put(match_id, currentMatch);
    }
  }
