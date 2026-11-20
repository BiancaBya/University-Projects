package com.mobile.backend.service;

import com.mobile.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository repo;
    public JpaUserDetailsService(UserRepository repo){ this.repo = repo; }

    @Override public UserDetails loadUserByUsername(String username) {
        var u = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("not found"));
        return User.withUsername(u.getUsername()).password(u.getPassword()).roles("USER").build();
    }
}
