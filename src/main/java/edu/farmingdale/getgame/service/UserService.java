package edu.farmingdale.getgame.service;

import edu.farmingdale.getgame.dto.UserDto;
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
