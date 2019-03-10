package com.ratiose.testtask.repository;

import com.ratiose.testtask.entity.Actor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ActorRepository extends CrudRepository<Actor, Long>
{
    @Query("SELECT a FROM Actor a WHERE a.code=:code")
    Actor findByCode(@Param("code") String code);
    @Query("SELECT a FROM Actor a WHERE a.name=:name")
    Actor findByName(@Param("name") String name);
}
