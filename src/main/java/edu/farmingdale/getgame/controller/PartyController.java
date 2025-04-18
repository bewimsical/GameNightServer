package edu.farmingdale.getgame.controller;

import edu.farmingdale.getgame.dto.PartyDto;
import edu.farmingdale.getgame.exception.ResourceNotFoundException;
import edu.farmingdale.getgame.model.Party;
import edu.farmingdale.getgame.model.User;
import edu.farmingdale.getgame.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public List<User> getPartyUsers(@PathVariable Long id){
        return partyService.getPartyUsers(id);
    }
    @GetMapping("/{id}/hosts")
    public List<User> getPartyHosts(@PathVariable Long id){
        return partyService.getPartyHosts(id);
    }
    @PostMapping("/edit/user")
    public void editUser(@RequestParam Long user, @RequestParam Long party, @RequestParam boolean host){
        partyService.changeUserPrivlages(user, party, host);
    }
    @DeleteMapping("/delete/user")
    public void deleteUser(@RequestParam Long user, @RequestParam Long party){
        partyService.deleteUser(user, party);
    }
}
