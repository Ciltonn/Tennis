package service;

import model.CurrentMatch;

public interface FinishedMatchesPersistenceService {
    void finishMatch(CurrentMatch currentMatch);

}
