package com.jwt.auth.jwtauth.config;

import com.jwt.auth.jwtauth.entity.UserInfo;
import com.jwt.auth.jwtauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoDetaileService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Optional<UserInfo> userInfo = userRepository.findByName(username);
     return userInfo.map(UserInfoUserDetails :: new)
              .orElseThrow(()->new UsernameNotFoundException("User not Found"));
    }
}
