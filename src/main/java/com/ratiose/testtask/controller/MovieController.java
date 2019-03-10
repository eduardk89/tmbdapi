package com.ratiose.testtask.controller;

import com.ratiose.testtask.entity.User;
import com.ratiose.testtask.responsedata.ResponseDataObject;
import com.ratiose.testtask.service.MovieService;
import com.ratiose.testtask.service.UserService;
import com.ratiose.testtask.validator.SimpleParamsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.ratiose.testtask.controller.ControllerConstants.WRONG_CREDENTIALS;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/movie")
public class MovieController
{
    private final UserService userService;
    private final MovieService movieService;
    private final SimpleParamsValidator validator;

    @RequestMapping(value = "/popular", method = POST)
    public ResponseEntity popular(@RequestParam String email, @RequestParam String password, HttpSession session)
    {
        if (userService.findUser(email, password) == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_CREDENTIALS);
        }
        ResponseDataObject responseObject = movieService.popularMovies();
        return ResponseEntity.status(responseObject.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON).body(responseObject);
    }

    @RequestMapping(value = "/add-to-watched-list", method = POST)
    public ResponseEntity addToWatchedList(@RequestParam String email, @RequestParam String password ,
                                           @RequestParam String movieName, HttpSession session)
    {
        User user = userService.findUser(email, password);
        if (user == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_CREDENTIALS);
        }
        List<String> validationResult = validator.validate(movieName.trim());
        if (!validationResult.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
        }
        ResponseDataObject responseObject = movieService.addMovieToWatched(email, movieName.trim());
        return ResponseEntity.status(responseObject.getHttpStatus()).body(responseObject);
    }

    @RequestMapping(value = "/search-by-date-unviewed-with-favorite-actors", method = POST)
    public ResponseEntity searchByDate(@RequestParam String email, @RequestParam String password,
                                       @RequestParam String year, @RequestParam String month, HttpSession session)
    {
        User user = userService.findUser(email, password);
        if (user == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_CREDENTIALS);
        }
        List<String> validationResult = validator.validate(year.trim(), month.trim());
        if (!validationResult.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
        }
        ResponseDataObject responseObject = movieService.searchUnviewedByDate(user, year.trim(), month.trim());
        return ResponseEntity.status(responseObject.getHttpStatus()).body(responseObject);
    }

    @Autowired
    public MovieController(UserService userService, MovieService movieService, SimpleParamsValidator validator)
    {
        this.userService = userService;
        this.movieService = movieService;
        this.validator = validator;
    }
}
