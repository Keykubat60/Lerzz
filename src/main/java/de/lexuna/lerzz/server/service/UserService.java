package de.lexuna.lerzz.server.service;

import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for registering new user accounts and checking if a given email already exists in the system.
 */
@Service
@Transactional
public class UserService  {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * Registers a new user account with the given user data.
     *
     * @param userDto the user data to register
     * @return the registered user object
     * @throws UserAlreadyExistException if a user with the same email address already exists in the system
     */
    public User registerNewUserAccount(User userDto) throws UserAlreadyExistException {
        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDto.getEmail());
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.getRoles().add("USER");

        return repository.save(user);
    }

    /**
     * Checks if a given email address already exists in the system.
     *
     * @param email the email address to check
     * @return {@code true} if the email already exists, {@code false} otherwise
     */
    private boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }
}
