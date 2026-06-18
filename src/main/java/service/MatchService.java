package service;

import entity.TennisMatch;

import java.util.List;

public interface MatchService {
    List<TennisMatch> getMatches(String filterByName, int page, int size);
    long getTotalMatchesCount(String filterByName);
}
