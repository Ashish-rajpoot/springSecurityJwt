package com.jwt.auth.jwtauth.services;

import com.jwt.auth.jwtauth.dto.Product;
import com.jwt.auth.jwtauth.entity.UserInfo;
import com.jwt.auth.jwtauth.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductServices {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    List<Product> productsList = null;

    @PostConstruct
    public void loadProductFromDB(){
        productsList = IntStream.rangeClosed(1,100)
                .mapToObj(i -> Product.builder()
                        .productId(i)
                        .name("product " + i)
                        .qty(new Random().nextInt(10))
                        .price(new Random().nextInt(5000))
                        .build()
                ).collect(Collectors.toList());
    }

    public List<Product> getProducts(){
        return productsList;
    }

    public Product getProductById(int id){
        return productsList.stream()
                .filter(products -> products.getProductId()==id)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Product " + id + " not found!"));
    }

    public String addUser(UserInfo userInfo){
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userRepository.save(userInfo);
        return "User added to System";
    }
}
