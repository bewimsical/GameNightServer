package edu.farmingdale.getgame.service;

import edu.farmingdale.getgame.dto.PartyDto;
import edu.farmingdale.getgame.dto.UserDto;
import edu.farmingdale.getgame.exception.ResourceNotFoundException;
import edu.farmingdale.getgame.model.*;
import edu.farmingdale.getgame.repository.PartyRepository;
import edu.farmingdale.getgame.repository.UserPartyRepository;
import edu.farmingdale.getgame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final UserPartyRepository userPartyRepository;
    private final UserRepository userRepository;

    @Autowired
    public PartyService(PartyRepository partyRepository, UserPartyRepository userPartyRepository, UserRepository userRepository) {
        this.partyRepository = partyRepository;
        this.userPartyRepository = userPartyRepository;
        this.userRepository = userRepository;
    }

    List<Party> getAllParties(){
        return partyRepository.findAll();
    }
    public Optional<Party> getParty(Long id){
        return partyRepository.findById(id);
    }
    public Party saveParty(Party party){
        return partyRepository.save(party);
    }
    public void deleteParty(Party party){
        partyRepository.delete(party);
    }
    public Party createParty(PartyDto partyDto, Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not Found"));
        Party party = new Party();
        party.setPartyName(partyDto.getPartyName());
        party.setPartyDate(partyDto.getPartyDate());
        party.setLocation(partyDto.getLocation());

        Party savedParty = partyRepository.save(party);

        UserParty userParty = new UserParty();
        userParty.setId(new UserPartyId(party.getPartyId(), user.getUserId()));
        userParty.setUser(user);
        userParty.setParty(party);
        userParty.setPartyHost(true);

        userPartyRepository.save(userParty);

        return savedParty;
    }

    public List<User> getPartyUsers(Long partyId){
        return userPartyRepository.findByParty_PartyId(partyId).stream()
                .map(UserParty::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getPartyHosts(Long partyId){
        return userPartyRepository.findByParty_PartyIdAndPartyHostTrue(partyId).stream()
                .map(UserParty::getUser)
                .collect(Collectors.toList());
    }
    //TODO fix exception handling
    public void addUser(Long userId, Long partyId, boolean isHost){
        Optional<User> opUser = userRepository.findById(userId);
        Optional<Party> opParty = getParty(partyId);

        User user = opUser.orElseThrow(() -> new ResourceNotFoundException("User not Found"));
        Party party = opParty.orElseThrow(() -> new ResourceNotFoundException("Party not Found"));

        UserParty userParty = new UserParty();
        userParty.setId(new UserPartyId(party.getPartyId(), user.getUserId()));
        userParty.setUser(user);
        userParty.setParty(party);
        userParty.setPartyHost(isHost);

        if (userPartyRepository.existsByUserAndParty(user,party)){
            throw new ResourceNotFoundException("User already added to this party");
        }
        userPartyRepository.save(userParty);
    }

    public void deleteUser(Long userId, Long partyID){
        Optional<UserParty> opUserParty = userPartyRepository.findByUser_UserIdAndParty_PartyId(userId,partyID);
        UserParty userParty = opUserParty.orElseThrow(()-> new ResourceNotFoundException("The user does not belong to this party"));
        if (userParty.getPartyHost()) {
            throw new IllegalStateException("Hosts cannot be removed from their own party.");
        }
        userPartyRepository.delete(userParty);
    }

    public void changeUserPrivlages(Long userId, Long partyID, boolean isHost){
        Optional<UserParty> opUserParty = userPartyRepository.findByUser_UserIdAndParty_PartyId(userId,partyID);
        UserParty userParty = opUserParty.orElseThrow(()-> new ResourceNotFoundException("The user does not belong to this party"));

        userParty.setPartyHost(isHost);
        userPartyRepository.save(userParty);
    }




}
