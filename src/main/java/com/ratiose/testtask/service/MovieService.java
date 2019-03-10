package com.ratiose.testtask.service;

import com.ratiose.testtask.entity.User;
import com.ratiose.testtask.responsedata.ResponseDataObject;

public interface MovieService
{
    ResponseDataObject popularMovies();
    ResponseDataObject addMovieToWatched(final String email, final String movieName);
    ResponseDataObject searchUnviewedByDate(User user, String year, String month);
}
