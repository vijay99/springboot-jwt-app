package com.jwt.conttoller;

import com.jwt.model.JwtRequest;
import com.jwt.model.JwtResponse;
import com.jwt.service.UserService;
import com.jwt.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/app")
    public String hello(){
        return "Welcome to Spring Security app";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUserName(),
                            jwtRequest.getPassword()
                    )
            );
        }
        catch (BadCredentialsException e){
            throw  new Exception("INVALID_CREDENTIALS",e);
        }

        final UserDetails userDetails =
                userService.loadUserByUsername(jwtRequest.getUserName());

        final String token=jwtUtility.generateToken(userDetails);

        return new JwtResponse(token);
    }
}
