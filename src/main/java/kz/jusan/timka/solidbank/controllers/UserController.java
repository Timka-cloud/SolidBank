package kz.jusan.timka.solidbank.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kz.jusan.timka.solidbank.entities.User;
import kz.jusan.timka.solidbank.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "BasicAuth")
public class UserController {
    private final UserService userService;


    @GetMapping("users/info")
    public User findById(Principal principal) {
        return userService.findByUsername(principal.getName());

    }


}