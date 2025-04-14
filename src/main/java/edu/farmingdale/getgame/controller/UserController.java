package edu.farmingdale.getgame.controller;

import edu.farmingdale.getgame.dto.UserDto;
import edu.farmingdale.getgame.exception.ResourceNotFoundException;
import edu.farmingdale.getgame.model.Game;
import edu.farmingdale.getgame.model.Party;
import edu.farmingdale.getgame.model.User;
import edu.farmingdale.getgame.service.GameService;
import edu.farmingdale.getgame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id){
        long userId = Long.parseLong(id);
        Optional<User> user = userService.getUser(userId);
        return user.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    @GetMapping("/{id}/games")
    public Set<Game> getGames(@PathVariable Long id){
        return userService.getUserGames(id);
    }
    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable Long id){
        return userService.getUserFriends(id);
    }
    @GetMapping("/{id}/parties")
    public List<Party> getParties(@PathVariable Long id){
        return userService.getUserParties(id);
    }
    @GetMapping("/{id}/hosted")
    public List<Party> getHostedParties(@PathVariable Long id){
        return userService.getHostedParties(id);
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




}
