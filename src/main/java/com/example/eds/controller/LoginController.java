package com.example.eds.controller;

import com.example.eds.entity.User;
import com.example.eds.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //@GetMapping("/register")
    //public String registerForm() {
        //return "register";
    //}

    //@PostMapping("/register")
    //public String register(@RequestParam String username, @RequestParam String password) {
        //User user = new User();
        //user.setUsername(username);
        //user.setPassword(passwordEncoder.encode(password));
        //user.setRole("ROLE_USER");

        //userRepository.save(user);

        //return "redirect:/login";
    //}
}
