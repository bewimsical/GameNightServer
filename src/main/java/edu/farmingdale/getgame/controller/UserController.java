package edu.farmingdale.getgame.controller;

import edu.farmingdale.getgame.dto.LoginRequest;
import edu.farmingdale.getgame.dto.PartyDto;
import edu.farmingdale.getgame.dto.UserDto;
import edu.farmingdale.getgame.exception.ResourceNotFoundException;
import edu.farmingdale.getgame.model.Game;
import edu.farmingdale.getgame.model.Party;
import edu.farmingdale.getgame.model.User;
import edu.farmingdale.getgame.service.GameService;
import edu.farmingdale.getgame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("/create")
    public UserDto createUser(@RequestBody UserDto userDto){
        System.out.println(userDto.getUsername());
        User user = userService.createUser(userDto);
        UserDto userdto =  new UserDto(user.getUserId(), user.getUsername(), user.getfName(), user.getlName(), user.getEmail(), user.getProfilePicUrl(), user.getUserPassword());
        System.out.println(userdto.toString());
        return userdto;
    }
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable String id){
        long userId = Long.parseLong(id);
        User user = userService.getUser(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        //return new UserDto(user.getUserId(), user.getUsername(), user.getfName(), user.getlName(), user.getEmail(), user.getProfilePicUrl(), user.getUserPassword());
        UserDto userdto =  new UserDto(user.getUserId(), user.getUsername(), user.getfName(), user.getlName(), user.getEmail(), user.getProfilePicUrl(), user.getUserPassword());
        System.out.println(userdto.toString());
        return userdto;
    }

    @GetMapping("/{id}/games")
    public Set<Game> getGames(@PathVariable Long id){
        return userService.getUserGames(id);
    }
    @GetMapping("/{id}/friends")
    public Set<UserDto> getFriends(@PathVariable Long id){
        Set<User> friends = userService.getUserFriends(id);
        Set<UserDto> friendsDtos = new HashSet<>();
        for (User user : friends){
            friendsDtos.add(new UserDto(user.getUserId(), user.getUsername(), user.getfName(), user.getlName(), user.getEmail(), user.getProfilePicUrl(), user.getUserPassword()));
        }
        return friendsDtos;
    }
    @GetMapping("/{id}/parties")
    public List<PartyDto> getParties(@PathVariable Long id){
        List<Party> parties =  userService.getUserParties(id);
        List<PartyDto> partyDtos = new ArrayList<>();
        for(Party p:parties){
            partyDtos.add(new PartyDto(p.getPartyId(), p.getPartyName(),p.getPartyDate(),p.getLocation()));
        }
        return partyDtos;
    }
    @GetMapping("/{id}/hosted")
    public List<PartyDto> getHostedParties(@PathVariable Long id){
        List<Party> parties =  userService.getHostedParties(id);
        List<PartyDto> partyDtos = new ArrayList<>();
        for(Party p:parties){
            partyDtos.add(new PartyDto(p.getPartyId(), p.getPartyName(),p.getPartyDate(),p.getLocation()));
        }
        return partyDtos;
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
    @GetMapping("/search/{username}")
    public List<UserDto> searchUserByUsername(@PathVariable String username){
        List<User> results = userService.findUserByUsername(username);
        List<UserDto> users = new ArrayList<>();

        for (User user : results){
            users.add(new UserDto(user.getUserId(), user.getUsername(), user.getfName(), user.getlName(), user.getEmail(), user.getProfilePicUrl(), user.getUserPassword()));
        }
        return users;
    }



    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Optional<User> userOpt = userService.getUserByEmail(email);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("Invalid credentials");
        }

        User user = userOpt.get();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            throw new ResourceNotFoundException("Invalid credentials");
        }

        return new UserDto(user.getUserId(), user.getUsername(), user.getfName(),
                user.getlName(), user.getEmail(), user.getProfilePicUrl(), null);
    }





}
