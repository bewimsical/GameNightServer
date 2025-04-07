package edu.farmingdale.getgame.controller;

import edu.farmingdale.getgame.dto.UserDto;
import edu.farmingdale.getgame.model.User;
import edu.farmingdale.getgame.service.GameService;
import edu.farmingdale.getgame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/create")
    public User createUser(@RequestBody UserDto userDto){
        System.out.println(userDto.getUsername());
        return userService.createUser(userDto);
    }

}
