package com.ratiose.testtask.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Movie
{

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String code;

    private String name;

    @ManyToMany(mappedBy = "moviesWatched")
    private Set<User> usersWatched;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set<User> getUsersWatched()
    {
        return usersWatched;
    }

    public void setUsersWatched(Set<User> usersWatched)
    {
        this.usersWatched = usersWatched;
    }
}
