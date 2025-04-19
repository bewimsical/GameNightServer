package edu.farmingdale.getgame.controller;

import edu.farmingdale.getgame.dto.UserDto;
import edu.farmingdale.getgame.exception.ResourceNotFoundException;
import edu.farmingdale.getgame.model.User;
import edu.farmingdale.getgame.service.GameService;
import edu.farmingdale.getgame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    //efrat TEMP
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userService.loginUser(email, password);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


/**
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) {
        Optional<User> userOpt = userService.getAllUsers().stream()
                .filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password))
                .findFirst();

        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
    */




}
