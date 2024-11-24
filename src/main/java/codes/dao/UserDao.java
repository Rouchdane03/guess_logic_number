package codes.dao;

import codes.entity.User;
import codes.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    private UserRepository userRepository;

    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void insertUser(User user){
        userRepository.save(user);
    }

    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public void deleteUser(int id){
        userRepository.deleteById(id);
    }

    public boolean isIdExists(int id){
        return userRepository.existsUserById(id);
    }

    public boolean isUsernameExists(String username){
        return userRepository.existsUserByUsername(username);
    }

}
