package kz.jusan.timka.solidbank.controllers;

import kz.jusan.timka.solidbank.dto.JwtRequest;
import kz.jusan.timka.solidbank.dto.JwtResponse;
import kz.jusan.timka.solidbank.responses.BodyResponse;
import kz.jusan.timka.solidbank.responses.ResponseMessage;
import kz.jusan.timka.solidbank.services.UserService;
import kz.jusan.timka.solidbank.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ResponseMessage( "Incorrect username or password",HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public BodyResponse register(@RequestBody JwtRequest jwtRequest) {
        return userService.register(jwtRequest);
    }
}