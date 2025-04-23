package edu.farmingdale.getgame.service;

import edu.farmingdale.getgame.dto.PartyDto;
import edu.farmingdale.getgame.dto.UserDto;
import edu.farmingdale.getgame.exception.ResourceNotFoundException;
import edu.farmingdale.getgame.model.Game;
import edu.farmingdale.getgame.model.Party;
import edu.farmingdale.getgame.model.User;
import edu.farmingdale.getgame.model.UserParty;
import edu.farmingdale.getgame.repository.GameRepository;
import edu.farmingdale.getgame.repository.PartyRepository;
import edu.farmingdale.getgame.repository.UserPartyRepository;
import edu.farmingdale.getgame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserPartyRepository userPartyRepository;
    private final PartyRepository partyRepository;

    @Autowired
    public UserService(UserRepository userRepository, GameRepository gameRepository, UserPartyRepository userPartyRepository, PartyRepository partyRepository){
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userPartyRepository = userPartyRepository;
        this.partyRepository = partyRepository;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }
    public User saveUser(User user){
        return userRepository.save(user);
    }
    public void deleteUser(User user){
        userRepository.delete(user);
    }
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(UserDto userDto) {
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already registered.");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setfName(userDto.getfName());
        user.setlName(userDto.getlName());
        user.setProfilePicUrl(userDto.getProfilePicUrl());
        //password hasher
        String hashedPassword = passwordEncoder.encode(userDto.getUserPassword());
        user.setUserPassword(hashedPassword);

        return saveUser(user);
    }



    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Set<Game> getUserGames(Long id){
        Optional<User> opUser = getUser(id);
        User user = opUser.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return user.getGames();
    }

    public void addGame(Long userId, int gameId){
        Optional<User> opUser = getUser(userId);
        Optional<Game> opGame = gameRepository.findById(gameId);

        User user = opUser.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Game game = opGame.orElseThrow(()-> new ResourceNotFoundException("Game not found with id: " + gameId));
        user.getGames().add(game);
        saveUser(user);
    }

    public void removeGame(Long userId, int gameId){
        Optional<User> opUser = getUser(userId);
        Optional<Game> opGame = gameRepository.findById(gameId);

        User user = opUser.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Game game = opGame.orElseThrow(()-> new ResourceNotFoundException("Game not found with id: " + gameId));

        if (user.getGames().remove(game)) {
            saveUser(user);
        }else{
            throw new ResourceNotFoundException("User does not have this game");
        }
    }

    public Set<User> getUserFriends(Long id){
        Optional<User> opUser = getUser(id);
        User user = opUser.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return user.getFriends();
    }
    public void addFriend(Long userId, Long friendId){

        Optional<User> opUser = getUser(userId);
        Optional<User> opFriend = userRepository.findById(friendId);

        User user = opUser.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        User friend = opFriend.orElseThrow(()-> new ResourceNotFoundException("Friend not found with id: " + friendId));

        user.getFriends().add(friend);

        saveUser(user);
    }
    public void removeFriend(Long userId, Long friendId){
        Optional<User> opUser = getUser(userId);
        Optional<User> opFriend = userRepository.findById(friendId);

        User user = opUser.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        User friend = opFriend.orElseThrow(()-> new ResourceNotFoundException("Friend not found with id: " + friendId));

        if (user.getFriends().remove(friend)) {
            saveUser(user);
        }else{
            throw new ResourceNotFoundException("User does not have this Friend");
        }
    }

    public List<Party> getUserParties(Long userId){
        return userPartyRepository.findByUser_UserId(userId).stream()
                .map(UserParty::getParty)
                .collect(Collectors.toList());
    }

    public List<Party> getHostedParties(Long userId){
        return userPartyRepository.findByUser_UserIdAndPartyHostTrue(userId).stream()
                .map(UserParty::getParty)
                .collect(Collectors.toList());
    }

}
