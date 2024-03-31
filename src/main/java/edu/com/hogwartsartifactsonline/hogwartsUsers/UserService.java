package edu.com.hogwartsartifactsonline.hogwartsUsers;

import edu.com.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public HogwartsUser findById(Integer userId){
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public List<HogwartsUser> findAll(){
        return this.userRepository.findAll();
    }

    public HogwartsUser save(HogwartsUser user){
        return this.userRepository.save(user);
    }

    public HogwartsUser update(Integer userId, HogwartsUser user){
        HogwartsUser foundUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        foundUser.setUsername(user.getUsername());
        foundUser.setEnabled(user.isEnabled());
        foundUser.setRoles(user.getRoles());

        return this.userRepository.save(foundUser);
    }

    public void delete(Integer userId){
        HogwartsUser foundUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);
    }
}