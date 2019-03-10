package com.ratiose.testtask.repository;

import com.ratiose.testtask.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long>
{
    @Query("SELECT u FROM User u WHERE u.email=:email")
    User findByEmail(@Param("email") String email);
}