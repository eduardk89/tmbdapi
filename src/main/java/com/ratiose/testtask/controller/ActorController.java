package com.ratiose.testtask.controller;

import com.ratiose.testtask.entity.User;
import com.ratiose.testtask.responsedata.ResponseDataObject;
import com.ratiose.testtask.service.ActorService;
import com.ratiose.testtask.service.UserService;
import com.ratiose.testtask.validator.SimpleParamsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.ratiose.testtask.controller.ControllerConstants.WRONG_CREDENTIALS;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/actor")
public class ActorController
{
    private final UserService userService;
    private final ActorService actorService;
    private final SimpleParamsValidator validator;

    @RequestMapping(value = "/add-to-favorites", method = POST)
    public ResponseEntity addFavoriteActor(@RequestParam String email, @RequestParam String password,
                                           @RequestParam String actorName, HttpSession session)
    {

        User user = userService.findUser(email, password);
        if (user == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_CREDENTIALS);
        }
        List<String> validationResult = validator.validate(actorName.trim());
        if (!validationResult.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
        }
        ResponseDataObject responseObject = actorService.addActorToFavorites(email, actorName.trim());
        return ResponseEntity.status(responseObject.getHttpStatus()).body(responseObject);
    }

    @RequestMapping(value = "/remove-from-favorites", method = POST)
    public ResponseEntity removeFavoriteActor(@RequestParam String email, @RequestParam String password,
                                              @RequestParam String actorName, HttpSession session)
    {

        User user = userService.findUser(email, password);
        if (user == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_CREDENTIALS);
        }
        List<String> validationResult = validator.validate(actorName.trim());
        if (!validationResult.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
        }
        ResponseDataObject responseObject = actorService.removeActorFromFavorites(email, actorName.trim());
        return ResponseEntity.status(responseObject.getHttpStatus()).body(responseObject);
    }

    @Autowired
    public ActorController(UserService userService, ActorService actorService, SimpleParamsValidator validator)
    {
        this.userService = userService;
        this.actorService = actorService;
        this.validator = validator;
    }
}
