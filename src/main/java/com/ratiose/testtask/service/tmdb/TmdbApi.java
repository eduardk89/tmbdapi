package com.ratiose.testtask.service.tmdb;

import com.ratiose.testtask.excecption.TmdbApiException;
import com.ratiose.testtask.service.tmdb.pojo.ActorObject;
import com.ratiose.testtask.service.tmdb.pojo.MovieObject;
import java.util.List;

public interface TmdbApi
{
    List<MovieObject> popularMovies() throws TmdbApiException;
    List<ActorObject> getActorByName (String actorName) throws TmdbApiException;
    List<MovieObject> getMoviesByName(String movieName) throws TmdbApiException;
    List<MovieObject> getMoviesByDateAndActors (List<String> actors,
                                                String startDate, String endDate) throws TmdbApiException;
}
