package com.chat.app.service;

import com.chat.app.entity.UserEntity;
import com.chat.app.model.User;
import com.chat.app.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserEntity register(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        UserEntity entity = new UserEntity(username, passwordEncoder.encode(rawPassword));
        return userRepository.save(entity);
    }

    @Override
    @Cacheable(value = "users", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new User(entity);
    }

    @CacheEvict(value = "users", key = "#username")
    public void evictUserCache(String username) {}
}