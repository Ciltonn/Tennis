package service;

import model.CurrentMatch;

public class NewMatchServiceImpl implements NewMatchService {
    @Override
    public CurrentMatch createNewMatch(String firstPlayer, String secondPlayer) {
        return new CurrentMatch(firstPlayer, secondPlayer);
    }
}
