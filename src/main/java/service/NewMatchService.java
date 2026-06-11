package service;

import model.CurrentMatch;
import entity.Player;
import static util.ValidationUtil.validate;


public class NewMatchService {

    public CurrentMatch createNewMatch(String firstPlayer, String secondPlayer) {
       return new CurrentMatch(firstPlayer, secondPlayer);
    }
}
