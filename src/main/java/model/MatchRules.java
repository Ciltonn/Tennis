package model;

public class MatchRules {
    private static final int MIN_POINT_DIFFERENCE = 2;
    private static final int MIN_GAMES_TO_WIN_TIEBREAK = 7;
    private static final int MIN_GAMES_TO_WIN_SET = 6;
    private static final int SETS_TO_WIN_MATCH = 2;

    MatchState matchState = MatchState.REGULAR;

   private  void pointWinBy(CurrentMatch currentMatch, Long playerId) {
       boolean isFirstPlayer = hasFirstPlayer(currentMatch, playerId);
       switch(matchState) {
           case REGULAR -> regularGame(currentMatch, isFirstPlayer);
           break;
           case ADVANTAGE -> advantageGame(currentMatch, isFirstPlayer);
           break;
           case TIE_BREAK -> tieBreakGame(currentMatch, isFirstPlayer);
           break;
           case MATCH_OVER -> throw new IllegalStateException("Match is over");
       }
       updatematchState(currentMatch);
   }
    private void regularGame(CurrentMatch currentMatch, boolean isFirstPlayer) {
        PlayerScore playerOne = scorePlayer(currentMatch, isFirstPlayer);
        PlayerScore playerTwo = scoreOpponent(currentMatch, isFirstPlayer);
        playerOne.setPoints();

    }
    boolean hasFirstPlayer(CurrentMatch currentMatch, Long playerId) {
       return currentMatch.getFirstPlayerScore().getPlayerId().equals(playerId);
    }
    private PlayerScore scorePlayer(CurrentMatch currentMatch, boolean isFirstPlayer) {
        return isFirstPlayer ? currentMatch.getFirstPlayerScore() : currentMatch.getSecondPlayerScore();
    }
    private PlayerScore scoreOpponent(CurrentMatch currentMatch, boolean isFirstPlayer) {
        return isFirstPlayer ? currentMatch.getFirstPlayerScore() : currentMatch.getSecondPlayerScore();
    }
    }


