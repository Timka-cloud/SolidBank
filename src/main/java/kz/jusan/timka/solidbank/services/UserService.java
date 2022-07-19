package kz.jusan.timka.solidbank.services;


import kz.jusan.timka.solidbank.dto.JwtRequest;
import kz.jusan.timka.solidbank.entities.Role;
import kz.jusan.timka.solidbank.entities.User;
import kz.jusan.timka.solidbank.exceptions.AccountNotFound;
import kz.jusan.timka.solidbank.repositories.RoleRepository;
import kz.jusan.timka.solidbank.repositories.UserRepository;
import kz.jusan.timka.solidbank.responses.BodyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found by " + username));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new AccountNotFound("Account not found by " + id));
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public BodyResponse register(JwtRequest jwtRequest) {
        Optional<User> user = userRepository.findUserByUsername(jwtRequest.getUsername());
        if (user.isPresent()) {
            return new BodyResponse("User exists", Response.Status.CONFLICT, null);
        }

        User savedUser = new User();
        savedUser.setUsername(jwtRequest.getUsername());
        savedUser.setPassword(passwordEncoder.encode(jwtRequest.getPassword()));
        Collection<Role> collection = new ArrayList<>();
        collection.add(roleRepository.findById(1L).get());
        savedUser.setRoles(collection);

        return new BodyResponse("User created", Response.Status.OK, userRepository.save(savedUser));
    }
}