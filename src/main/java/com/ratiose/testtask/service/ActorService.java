package com.ratiose.testtask.service;

import com.ratiose.testtask.responsedata.ResponseDataObject;

public interface ActorService
{
    ResponseDataObject addActorToFavorites(final String email, final String actorName);
    ResponseDataObject removeActorFromFavorites(final String email, final String name);
}
