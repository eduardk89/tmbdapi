package com.ratiose.testtask.controller;

import com.ratiose.testtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.ratiose.testtask.controller.ControllerConstants.SUCCESS_REGISTRATION;
import static com.ratiose.testtask.controller.ControllerConstants.WRONG_CREDENTIALS;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/user")
public class UserController
{
    private final UserService userService;

    @RequestMapping(value = "/register", method = POST)
    public ResponseEntity registerUser(@RequestParam String email,
                                               @RequestParam String password,
                                               HttpSession session)
    {
        if (userService.registerUser(email, password) != null)
        {
            return ResponseEntity.status(HttpStatus.OK).body(SUCCESS_REGISTRATION);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_CREDENTIALS);
        }
    }

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }
}
