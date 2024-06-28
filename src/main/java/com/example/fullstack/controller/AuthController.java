package com.example.fullstack.controller;

import com.example.fullstack.Model.User;
import com.example.fullstack.Repository.UserRepository;
import com.example.fullstack.Request.LoginRequest;
import com.example.fullstack.Service.CustomUserDetailsImpl;
import com.example.fullstack.config.JwtProvider;
import com.example.fullstack.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsImpl customUserDetails;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {
        try {
            User isUserExist = userrepo.findByEmail(user.getEmail());
            if (isUserExist != null) {
                throw new Exception("email already exist with another account");
            }

            User createUser = new User();
            createUser.setName(user.getName());
            createUser.setEmail(user.getEmail());
            createUser.setPassword(passwordEncoder.encode(user.getPassword()));

            User saveduser = userrepo.save(createUser);

            Authentication authentication = new UsernamePasswordAuthenticationToken(saveduser.getEmail(), null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = JwtProvider.generateToken(authentication);

            AuthResponse res = new AuthResponse();
            res.setMessage("signup success");
            res.setJwt(jwt);

            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest){

        String username=loginRequest.getEmail();
        String password=loginRequest.getPassword();

        Authentication authentication=authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt= JwtProvider.generateToken(authentication);

        AuthResponse res=new AuthResponse();
        res.setMessage("signin success");
        res.setJwt(jwt);

        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

    private Authentication authenticate(String username, String password) {

        UserDetails userdetails=customUserDetails.loadUserByUsername(username);

        if(userdetails==null){
            throw new BadCredentialsException("Invalid Username");
        }

        if(!passwordEncoder.matches(password,userdetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(userdetails,null,userdetails.getAuthorities());
    }

}
