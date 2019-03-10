package com.ratiose.testtask.service.impl;

import com.ratiose.testtask.entity.Actor;
import com.ratiose.testtask.entity.User;
import com.ratiose.testtask.excecption.TmdbApiException;
import com.ratiose.testtask.repository.ActorRepository;
import com.ratiose.testtask.repository.UserRepository;
import com.ratiose.testtask.responsedata.ResponseDataObject;
import com.ratiose.testtask.service.ActorService;
import com.ratiose.testtask.service.tmdb.TmdbApi;
import com.ratiose.testtask.service.tmdb.pojo.ActorObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ActorServiceImpl implements ActorService
{
    private static final String WAS_ADDED_BEFORE = "This actor was already added to favorites before";
    private static final String NO_ACTOR_FOUND = "No actor found on your request";
    private static final String ADD_SUCCESS = "Success. Actor added to your favorites";
    private static final String REMOVE_SUCCESS = "Success. Actor removed from your favorites";

    private final UserRepository userRepository;
    private final ActorRepository actorRepository;
    private final TmdbApi tmdbApi;

    @Override
    public ResponseDataObject addActorToFavorites(final String email, final String actorName)
    {
        User user = userRepository.findByEmail(email);
        if(hasUserThisActor(user, actorName))
        {
            return new ResponseDataObject(HttpStatus.OK, WAS_ADDED_BEFORE);
        }
        List<ActorObject> actors;
        try
        {
            actors =  tmdbApi.getActorByName(actorName);
        } catch (TmdbApiException e)
        {
            return new ResponseDataObject(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        List<ActorObject> filteredActors = actors.stream()
                .filter(a -> a.getName().equalsIgnoreCase(actorName)).collect(toList());
        if (filteredActors.isEmpty())
        {
            return new ResponseDataObject(HttpStatus.BAD_REQUEST, NO_ACTOR_FOUND);
        }
        createActor(filteredActors, user);
        return new ResponseDataObject(HttpStatus.OK, ADD_SUCCESS);
    }

    @Override
    public ResponseDataObject removeActorFromFavorites(final String email, final String name)
    {
        User user = userRepository.findByEmail(email);
        if(hasUserThisActor(user, name))
        {
            user.getFavoriteActors().remove(actorRepository.findByName(name.toLowerCase()));
            userRepository.save(user);
            return new ResponseDataObject(HttpStatus.OK, REMOVE_SUCCESS);
        }
        return new ResponseDataObject(HttpStatus.BAD_REQUEST, NO_ACTOR_FOUND);
    }

    private void createActor(List<ActorObject> filteredActors, User user)
    {
        for (ActorObject actorObject : filteredActors)
        {
            Actor actor = new Actor();
            actor.setCode(String.valueOf(actorObject.getId()));
            actor.setName(actorObject.getName().toLowerCase());
            actorRepository.save(actor);
            user.getFavoriteActors().add(actor);
        }
        userRepository.save(user);
    }

    private boolean hasUserThisActor (final User user, final String name)
    {
        return user.getFavoriteActors()
                .contains(actorRepository.findByName(name.toLowerCase()));
    }

    @Autowired
    public ActorServiceImpl(UserRepository userRepository, ActorRepository actorRepository, TmdbApi tmdbApi)
    {
        this.userRepository = userRepository;
        this.actorRepository = actorRepository;
        this.tmdbApi = tmdbApi;
    }
}
