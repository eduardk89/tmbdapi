package com.ratiose.testtask.service;

import com.ratiose.testtask.entity.User;

public interface UserService
{
    User registerUser(String email, String password);
    User findUser(String email, String password);
}

