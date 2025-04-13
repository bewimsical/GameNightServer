package edu.farmingdale.getgame.service;

import edu.farmingdale.getgame.dto.UserDto;
import edu.farmingdale.getgame.exception.ResourceNotFoundException;
import edu.farmingdale.getgame.model.Game;
import edu.farmingdale.getgame.model.User;
import edu.farmingdale.getgame.repository.GameRepository;
import edu.farmingdale.getgame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Autowired
    public UserService(UserRepository userRepository, GameRepository gameRepository){
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }

    //TODO add error handling
    public Set<Game> getUserGames(Long id){
        Optional<User> user = getUser(id);
        if(user.isPresent()){
            return user.get().getGames();
        }else return null;
    }
    //TODO add error handling
    public void addGame(Long userId, int gameId){
        Optional<User> user = getUser(userId);
        Optional<Game> game = gameRepository.findById(gameId);

        user.get().getGames().add(game.get());

        saveUser(user.get());
    }
    //TODO add error handling
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
    //TODO add error handling
    public Set<User> getUserFriends(Long id){
        Optional<User> user = getUser(id);
        if(user.isPresent()){
            return user.get().getFriends();
        }else return null;
    }
    //TODO add error handling
    public void addFriend(Long userId, Long friendId){
        Optional<User> user = getUser(userId);
        Optional<User> friend = userRepository.findById(friendId);

        user.get().getFriends().add(friend.get());

        saveUser(user.get());
    }
    //TODO add error handling
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
    public User saveUser(User user){
        return userRepository.save(user);
    }
    public void deleteUser(User user){
        userRepository.delete(user);
    }
    public User createUser(UserDto userDto){
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setfName(userDto.getfName());
        user.setlName(userDto.getlName());
        user.setProfilePicUrl(userDto.getProfilePicUrl());
        return saveUser(user);
    }
}
