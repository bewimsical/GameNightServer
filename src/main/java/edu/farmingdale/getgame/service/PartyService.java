package edu.farmingdale.getgame.service;

import edu.farmingdale.getgame.dto.GameCountDto;
import edu.farmingdale.getgame.dto.PartyDto;
import edu.farmingdale.getgame.exception.ResourceNotFoundException;
import edu.farmingdale.getgame.model.*;
import edu.farmingdale.getgame.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final UserPartyRepository userPartyRepository;
    private final UserRepository userRepository;
    private final GamePartyRepository gamePartyRepository;
    private  final GameRepository gameRepository;

    @Autowired
    public PartyService(PartyRepository partyRepository, UserPartyRepository userPartyRepository, UserRepository userRepository, GamePartyRepository gamePartyRepository, GameRepository gameRepository ) {
        this.partyRepository = partyRepository;
        this.userPartyRepository = userPartyRepository;
        this.userRepository = userRepository;
        this.gamePartyRepository = gamePartyRepository;
        this.gameRepository = gameRepository;
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

    public List<GameCountDto> getGameCount(Long partyId) {
        List<Object[]> results = gamePartyRepository.findGameCountsByPartyId(partyId);
        List<GameCountDto> gameCountList = new ArrayList<>();

        for (Object[] row : results) {
            Integer gameId = (Integer) row[0];
            Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));
            Integer count = (Integer) row[1];
            GameCountDto gameDto = new GameCountDto(game.getGameId(), game.getGame_name(),game.getMinPlayers(), game.getMaxPlayers(), game.getPlayTime(), game.getImgUrl(), count);
            gameCountList.add(gameDto);
        }

        return gameCountList;
    }

    public void addGameToParty(int game, Long party) {
        GamePartyId id = new GamePartyId(game, party);
        GameParty gameParty = gamePartyRepository.findById(id).orElse(null);

        if (gameParty != null) {
            gameParty.setCount(gameParty.getCount() + 1);
        } else {
            Game gameObj = gameRepository.findById(game).orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + game));
            Party partyObj = partyRepository.findById(party).orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + party));

            gameParty = new GameParty(gameObj, partyObj);
        }
        gamePartyRepository.save(gameParty);
    }

    public void removeGameFromParty(int game, Long party) {
        GamePartyId id = new GamePartyId(game, party);
        GameParty gameParty = gamePartyRepository.findById(id).orElse(null);

        if (gameParty != null) {
            int updatedCount = gameParty.getCount() - 1;
            if (updatedCount <= 0) {
                gamePartyRepository.delete(gameParty);
            } else {
                gameParty.setCount(updatedCount);
                gamePartyRepository.save(gameParty);
            }
        }
    }




}
