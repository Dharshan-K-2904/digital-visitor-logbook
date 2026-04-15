package com.visitorlogbook.service;
import org.springframework.stereotype.Service;
import com.visitorlogbook.model.User;
import com.visitorlogbook.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        if (user != null) {
            return userRepository.save(user);
        }
        return null;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        return password.equals(user.getPassword()) ? user : null;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}