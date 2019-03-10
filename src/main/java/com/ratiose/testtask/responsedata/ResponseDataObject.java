package com.ratiose.testtask.responsedata;

import com.ratiose.testtask.service.tmdb.pojo.MovieObject;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ResponseDataObject
{
    private HttpStatus httpStatus;
    private String message;
    private List<MovieObject> searchResults;

    public ResponseDataObject(HttpStatus httpStatus, String message)
    {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ResponseDataObject(HttpStatus httpStatus, List<MovieObject> searchResults)
    {
        this.httpStatus = httpStatus;
        this.searchResults = searchResults;
    }

    public ResponseDataObject(HttpStatus httpStatus, String message, List<MovieObject> searchResults)
    {
        this.httpStatus = httpStatus;
        this.message = message;
        this.searchResults = searchResults;
    }

    public HttpStatus getHttpStatus()
    {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus)
    {
        this.httpStatus = httpStatus;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<MovieObject> getSearchResults()
    {
        return searchResults;
    }

    public void setSearchResults(List<MovieObject> searchResults)
    {
        this.searchResults = searchResults;
    }
}
