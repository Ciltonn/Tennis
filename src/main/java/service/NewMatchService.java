package service;

import model.CurrentMatch;

public interface NewMatchService {
    CurrentMatch createNewMatch(String firstPlayer, String secondPlayer);
}
