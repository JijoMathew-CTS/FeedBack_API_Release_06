package com.fms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fms.entity.User;
import com.fms.repo.UserRepository;

@Service
public class LoginService {
	@Autowired
    private UserRepository repo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
    }

    public User getUserByCredentials(String[] credentials) {

        boolean isValidUser = false;

        Optional<User> user = repo.findByEmail(credentials[0]);
        if (user.isPresent()) {
            isValidUser =
                    passwordEncoder.matches(credentials[1],user.get().getPassword());
        }
        if (isValidUser) {
            return user.get();
        }
        return null;
    }

    public boolean authenticate(String[] credentials) {

        boolean isValidUser = false;

        Optional<User> user = repo.findByEmail(credentials[0]);
        if (user.isPresent()) {
            isValidUser = user.get().getEmail().equals(credentials[0])
                    && passwordEncoder.matches(credentials[1], user.get().getPassword());
        }
        return isValidUser;
    }
}
