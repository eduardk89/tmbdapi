package com.ratiose.testtask.repository;

import com.ratiose.testtask.entity.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends CrudRepository<Movie, Long>
{
    @Query("SELECT m FROM Movie m WHERE m.name=:name")
    Movie findByName(@Param("name") String name);
}
