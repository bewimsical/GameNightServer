package edu.farmingdale.getgame.controller;

import edu.farmingdale.getgame.dto.UserDto;
import edu.farmingdale.getgame.exception.ResourceNotFoundException;
import edu.farmingdale.getgame.model.User;
import edu.farmingdale.getgame.service.GameService;
import edu.farmingdale.getgame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    @PostMapping("/addgame")
    public void addGame(@RequestParam Long user, @RequestParam int game){

        userService.addGame(user,game);
    }
    @DeleteMapping("deletegame")
    public void deleteGame(@RequestParam Long user, @RequestParam int game){
        userService.removeGame(user, game);
    }
    @PostMapping("/addfriend")
    public void addFriend(@RequestParam Long user, @RequestParam Long friend){

        userService.addFriend(user,friend);
    }
    @DeleteMapping("deletefriend")
    public void deleteFriend(@RequestParam Long user, @RequestParam Long friend){
        userService.removeFriend(user, friend);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id){
        long userId = Long.parseLong(id);
        Optional<User> user = userService.getUser(userId);
        return user.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }


}
