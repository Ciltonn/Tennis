package service;

import dao.MatchImpl;
import dao.PlayerImpl;
import entity.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchesService {
    private final MatchImpl match;
    private final PlayerImpl player;

    public MatchesService(MatchImpl match, PlayerImpl player) {
        this.match = match;
        this.player = player;
    }

    private List<Match> getMatches(int page, int pageSize) {
        return match.findAll();
    }

    private List<Match> paginate(List<Match> matches, int page, int pageSize) {
        if(matches==null) {
            return new ArrayList<>();
        }
        if(page<1) {
            page=1;
        }
        int start = (page-1)*pageSize;
        int end = Math.min(start+pageSize, matches.size());
        if(start>= matches.size()) {
            return new ArrayList<>();
        }
        return matches.subList(start, end);
    }

}
