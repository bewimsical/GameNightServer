package edu.farmingdale.getgame.controller;

import edu.farmingdale.getgame.dto.GameCountDto;
import edu.farmingdale.getgame.dto.PartyDto;
import edu.farmingdale.getgame.dto.UserDto;
import edu.farmingdale.getgame.exception.ResourceNotFoundException;
import edu.farmingdale.getgame.model.Game;
import edu.farmingdale.getgame.model.Party;
import edu.farmingdale.getgame.model.User;
import edu.farmingdale.getgame.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/parties")
public class PartyController {

    private final PartyService partyService;

    @Autowired
    public PartyController(PartyService partyService){
        this.partyService = partyService;
    }

    @PostMapping("/create/{id}")
    public Party createParty(@RequestBody PartyDto partyDto, @PathVariable Long id){
        return partyService.createParty(partyDto, id);
    }
    @GetMapping("/{id}")
    public Party getParty(@PathVariable Long id){
        return partyService.getParty(id).orElseThrow(()-> new ResourceNotFoundException("no party exists with this id"));
    }
    @DeleteMapping("delete/{id}")
    public void deleteParty(@PathVariable Long id){
        Party party = partyService.getParty(id).orElseThrow(()->new ResourceNotFoundException("no party with this id"));
        partyService.deleteParty(party);
    }
    @PostMapping("/adduser")
    public void addUser(@RequestParam Long user, @RequestParam Long party, @RequestParam boolean host){
        partyService.addUser(user, party, host);
    }
    @GetMapping("/{id}/users")
    public List<UserDto> getPartyUsers(@PathVariable Long id){
        List<User> users = partyService.getPartyUsers(id);
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users){
            userDtos.add(new UserDto(user.getUserId(), user.getUsername(), user.getfName(), user.getlName(), user.getEmail(), user.getProfilePicUrl(), user.getUserPassword()));
        }
        return userDtos;
    }
    @GetMapping("/{id}/hosts")
    public List<UserDto> getPartyHosts(@PathVariable Long id){
        List<User> users = partyService.getPartyHosts(id);
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users){
            userDtos.add(new UserDto(user.getUserId(), user.getUsername(), user.getfName(), user.getlName(), user.getEmail(), user.getProfilePicUrl(), user.getUserPassword()));
        }
        return userDtos;
    }
    @PostMapping("/edit/user")
    public void editUser(@RequestParam Long user, @RequestParam Long party, @RequestParam boolean host){
        partyService.changeUserPrivlages(user, party, host);
    }
    @DeleteMapping("/delete/user")
    public void deleteUser(@RequestParam Long user, @RequestParam Long party){
        partyService.deleteUser(user, party);
    }
    @PostMapping("{partyId}/games/{gameId}")
    public void selectGame(@PathVariable Long partyId, @PathVariable int gameId){
        partyService.addGameToParty(gameId, partyId);
    }
    @DeleteMapping("{partyId}/games/{gameId}")
    public void decrementGame(@PathVariable Long partyId, @PathVariable int gameId){
        partyService.removeGameFromParty(gameId, partyId);
    }
    @GetMapping("{partyId}/games")
    public List<GameCountDto> getSelectedGames(@PathVariable Long partyId){
        return partyService.getGameCount(partyId);
    }
}
