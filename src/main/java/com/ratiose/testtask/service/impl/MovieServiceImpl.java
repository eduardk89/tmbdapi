package com.ratiose.testtask.service.impl;

import com.ratiose.testtask.entity.Actor;
import com.ratiose.testtask.entity.Movie;
import com.ratiose.testtask.entity.User;
import com.ratiose.testtask.excecption.TmdbApiException;
import com.ratiose.testtask.repository.MovieRepository;
import com.ratiose.testtask.repository.UserRepository;
import com.ratiose.testtask.responsedata.ResponseDataObject;
import com.ratiose.testtask.service.MovieService;
import com.ratiose.testtask.service.tmdb.TmdbApi;
import com.ratiose.testtask.service.tmdb.pojo.MovieObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class MovieServiceImpl implements MovieService
{
    private static final String DATE_SEPARATOR = "-";
    private static final String FIRST_DAY_OF_MONTH = "01";
    private static final String MOVIE_WAS_ADDED_BEFORE = "This movie was already added to watched list before";
    private static final String MOVIE_ADD_SUCCESS = "This movie was successfully added to watched list";
    private static final String NO_MOVIE_FOUND = "No movie found on your request";
    private static final String NO_FAVORITE_ACTORS =
            "You have no favorite actors, and we populate all unwatched movies for selected date";

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TmdbApi tmdbApi;

    @Override
    public ResponseDataObject popularMovies()
    {
        List<MovieObject> movies;
        try
        {
            movies = tmdbApi.popularMovies();
        }
        catch (TmdbApiException e)
        {
            return new ResponseDataObject(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return new ResponseDataObject(HttpStatus.OK, movies);
    }

    @Override
    public ResponseDataObject addMovieToWatched(String email, String movieName)
    {
        User user = userRepository.findByEmail(email);
        if (hasUserThisMovieInWatched(user, movieName))
        {
            return new ResponseDataObject(HttpStatus.OK, MOVIE_WAS_ADDED_BEFORE);
        }
        List<MovieObject> searchResults;
        try
        {
            searchResults = tmdbApi.getMoviesByName(movieName);
        } catch (TmdbApiException e)
        {
            return new ResponseDataObject(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        List<MovieObject> movieExactResults = searchResults.stream()
                .filter(m -> m.getTitle().equalsIgnoreCase(movieName)).collect(toList());
        if (!movieExactResults.isEmpty())
        {
            addMovies(movieExactResults, user);
            return new ResponseDataObject(HttpStatus.OK, MOVIE_ADD_SUCCESS);
        }
        return new ResponseDataObject(HttpStatus.BAD_REQUEST, NO_MOVIE_FOUND);
    }

    @Override
    public ResponseDataObject searchUnviewedByDate(final User user, String year, String month)
    {
        String firstDayOfMounth = year + DATE_SEPARATOR + month + DATE_SEPARATOR + FIRST_DAY_OF_MONTH;
        String lastDayOfMounth = calculateLastDayOfMounth(firstDayOfMounth);
        List<String> actors = user.getFavoriteActors()
                .stream().map(Actor::getCode).collect(toList());

        List<MovieObject> unfilteredMovies;
        try
        {
            unfilteredMovies = tmdbApi.getMoviesByDateAndActors(actors, firstDayOfMounth, lastDayOfMounth);
        } catch (TmdbApiException e)
        {
            return new ResponseDataObject(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        List<MovieObject> filteredMovies = filterWatchedMovies(user, unfilteredMovies);
        if (filteredMovies.isEmpty())
        {
            return new ResponseDataObject(HttpStatus.BAD_REQUEST, NO_MOVIE_FOUND);
        }
        if (actors.isEmpty())
        {
            return new ResponseDataObject(HttpStatus.OK, NO_FAVORITE_ACTORS, filteredMovies);
        }
        return new ResponseDataObject(HttpStatus.OK, filteredMovies);
    }

    private void addMovies(List<MovieObject> movies, User user)
    {
        for (MovieObject movieObject : movies)
        {
            Movie movie = new Movie();
            movie.setCode(String.valueOf(movieObject.getId()));
            movie.setName(movieObject.getTitle().toLowerCase());
            movieRepository.save(movie);
            user.getMoviesWatched().add(movie);
        }
        userRepository.save(user);
    }

    private String calculateLastDayOfMounth(String firstDayOfMounth)
    {
        return LocalDate.parse(firstDayOfMounth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .with(TemporalAdjusters.lastDayOfMonth()).toString();
    }

    private boolean hasUserThisMovieInWatched(final User user, final String movieName)
    {
        return user.getMoviesWatched()
                .contains(movieRepository.findByName(movieName.toLowerCase()));
    }

    private List<MovieObject> filterWatchedMovies (final User user, List<MovieObject> unfilteredMovies)
    {
        return unfilteredMovies.stream().
                filter(m -> !(user.getMoviesWatched().stream().map(Movie::getCode).collect(toList()))
                        .contains(String.valueOf(m.getId()))).collect(toList());
    }

    @Autowired
    public MovieServiceImpl(UserRepository userRepository, MovieRepository movieRepository, TmdbApi tmdbApi)
    {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.tmdbApi = tmdbApi;
    }

}
