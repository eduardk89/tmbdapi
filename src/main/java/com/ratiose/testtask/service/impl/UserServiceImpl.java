package com.ratiose.testtask.service.impl;

import com.ratiose.testtask.entity.User;
import com.ratiose.testtask.repository.UserRepository;
import com.ratiose.testtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User registerUser(String email, String password)
    {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return null;
        }
        user = createUser(email, password);
        return userRepository.save(user);
    }

    @Override
    public User findUser(String email, String password)
    {
        User foundUser = userRepository.findByEmail(email);
        if (nonNull(foundUser)) {
            if (passwordEncoder.matches(password, foundUser.getPassword())) {
                return foundUser;
            }
        }
        return null;
    }

    private User createUser(String email, String password)
    {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}