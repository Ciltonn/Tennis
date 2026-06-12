package service;

import model.CurrentMatch;

public class NewMatchService {

    public CurrentMatch createNewMatch(String firstPlayer, String secondPlayer) {
       return new CurrentMatch(firstPlayer, secondPlayer);
    }
}
