package com.jwt.auth.jwtauth.controller;

import com.jwt.auth.jwtauth.config.UserInfoDetaileService;
import com.jwt.auth.jwtauth.dto.JWTRequest;
import com.jwt.auth.jwtauth.dto.JWTResponse;
import com.jwt.auth.jwtauth.dto.Product;
import com.jwt.auth.jwtauth.entity.UserInfo;
import com.jwt.auth.jwtauth.services.ProductServices;
import com.jwt.auth.jwtauth.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductServices productServices;
    @Autowired
    JwtUtility jwtUtility;
    @Autowired
    UserInfoDetaileService userInfoDetaileService;
    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to home page";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Product> getProducts() {
        return productServices.getProducts();

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Product getProductById(@PathVariable int id) {
        return productServices.getProductById(id);
    }

    @PostMapping("/new")
    public ResponseEntity<?> addNewUser(@RequestBody UserInfo userInfo) {
        productServices.addUser(userInfo);
        return ResponseEntity.ok("User Added");
    }

    @PostMapping("/authtoken")
    public JWTResponse  authentication(@RequestBody JWTRequest jwtRequest) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                jwtRequest.getName(),
                                jwtRequest.getPassword()
                        )
                );
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = userInfoDetaileService.loadUserByUsername(jwtRequest.getName());
            String token = jwtUtility.generateToken(userDetails);
            System.out.println(token);
            return new JWTResponse(token);
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }
}
