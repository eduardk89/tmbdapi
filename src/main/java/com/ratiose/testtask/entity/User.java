package com.ratiose.testtask.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String email;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "actors_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> favoriteActors;

    @ManyToMany
    @JoinTable(
            name = "users_watched",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> moviesWatched;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Set<Actor> getFavoriteActors()
    {
        return favoriteActors;
    }

    public void setFavoriteActors(Set<Actor> favoriteActors)
    {
        this.favoriteActors = favoriteActors;
    }

    public Set<Movie> getMoviesWatched()
    {
        return moviesWatched;
    }

    public void setMoviesWathed(Set<Movie> moviesWatched)
    {
        this.moviesWatched = moviesWatched;
    }
}