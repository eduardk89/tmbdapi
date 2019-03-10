package com.ratiose.testtask.service.tmdb.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratiose.testtask.excecption.TmdbApiException;
import com.ratiose.testtask.service.tmdb.pojo.ActorObject;
import com.ratiose.testtask.service.tmdb.pojo.MovieObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.ratiose.testtask.service.tmdb.TmdbApi;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class TmdbApiImpl implements TmdbApi
{
    private static final String EMPTY_RESULT = "[]";
    private static final String API_EXCEPTION_MESSAGE = "TMDB throw exception with status code: ";
    private static final String TOTAL_PAGES = "total_pages";
    private static final String RESULTS = "results";
    private static final String PAGE = "page";
    private static final String API_KEY = "api_key";
    private static final String LANG = "language";
    private static final String DISCOVER_MOVIE = "/discover/movie";
    private static final String SEARCH_MOVIE = "/search/movie";
    private static final String SEARCH_PERSON = "/search/person";
    private static final String MOVIE_POPULAR = "/movie/popular";
    private static final String WITH_CAST = "with_cast";
    private static final String RELEASE_DATE_LTE = "primary_release_date.lte";
    private static final String RELEASE_DATE_GTE = "primary_release_date.gte";
    private static final String QUERY = "query";

    @Value("${tmdb.apikey}")
    private String tmdbApiKey;
    @Value("${tmdb.language}")
    private String tmdbLanguage;
    @Value("${tmdb.api.base.url}")
    private String tmdbApiBaseUrl;
    @Value("${max.pages.allowed}")
    private int maxPagesAllowed;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<MovieObject> popularMovies() throws TmdbApiException
    {
        List<MovieObject> movies = new ArrayList<>();
        callTmdbAndHandleResult(MOVIE_POPULAR, new HashMap<>(), MovieObject[].class, movies);
        return movies;
    }

    @Override
    public List<ActorObject> getActorByName (String name) throws TmdbApiException
    {
        List<ActorObject> actors = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put(QUERY, name);
        callTmdbAndHandleResult(SEARCH_PERSON, params, ActorObject[].class, actors);
        return actors;
    }

    @Override
    public List<MovieObject> getMoviesByName(String movieName) throws TmdbApiException
    {
        List<MovieObject> movies = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put(QUERY, movieName);
        callTmdbAndHandleResult(SEARCH_MOVIE, params, MovieObject[].class, movies);
        return movies;
    }

    @Override
    public List<MovieObject> getMoviesByDateAndActors(List<String> actors,
                                                      String startDate, String endDate) throws TmdbApiException
    {
        List<MovieObject> movies = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put(RELEASE_DATE_GTE, startDate);
        params.put(RELEASE_DATE_LTE, endDate);
        if (!actors.isEmpty())
        {
            for(String actorCode : actors)
            {
                params.put(WITH_CAST, actorCode);
                callTmdbAndHandleResult(DISCOVER_MOVIE, params, MovieObject[].class, movies);
            }
        }
        else
        {
            callTmdbAndHandleResult(DISCOVER_MOVIE, params, MovieObject[].class, movies);
        }

        return movies;
    }

    private String getTmdbUrl(String tmdbItem, Map<String, String> paramsMap) throws URISyntaxException
    {
        URIBuilder uriBuilder = new URIBuilder(tmdbApiBaseUrl + tmdbItem);
        uriBuilder.addParameter(LANG, tmdbLanguage);
        uriBuilder.addParameter(API_KEY, tmdbApiKey);
        if(paramsMap != null && !paramsMap.isEmpty())
        {
            paramsMap.forEach(uriBuilder::addParameter);
        }
        return uriBuilder.build().toString();
    }

    private <T> void callTmdbAndHandleResult (final String path, Map<String, String> params,
                                              final Class<T[]> resourceClass, final List<T> results)
                                              throws TmdbApiException
    {
        boolean hasMorePages = false;
        int page = 1;
        params.put(PAGE, Integer.toString(page));
        try
        {
            do
            {
                String url = getTmdbUrl(path, params);
                HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
                checkStatus(jsonResponse);
                collectMovies(jsonResponse, results, resourceClass);
                hasMorePages = getTotalPages(jsonResponse) > page && page <= maxPagesAllowed;
                page += 1;
            }
            while (hasMorePages);
        }
        catch (UnirestException | URISyntaxException | IOException e)
        {
            e.printStackTrace();
        }
    }

    private void checkStatus(HttpResponse<JsonNode> jsonResponse) throws TmdbApiException
    {
        if (jsonResponse.getStatus() != HttpStatus.SC_OK)
        {
            throw new TmdbApiException(API_EXCEPTION_MESSAGE + jsonResponse.getStatus());
        }
    }

    private <T> void collectMovies(HttpResponse<JsonNode> jsonResponse,
                               final List<T> results, final Class<T[]> resourceClass) throws IOException
    {
        String resultsString = jsonResponse.getBody().getObject().get(RESULTS).toString();
        if (!EMPTY_RESULT.equals(resultsString))
        {
            results.addAll(Arrays.asList(mapper.readValue(resultsString, resourceClass)));
        }
    }

    private int getTotalPages(HttpResponse<JsonNode> jsonResponse)
    {
        return jsonResponse.getBody().getObject().getInt(TOTAL_PAGES);
    }
}
