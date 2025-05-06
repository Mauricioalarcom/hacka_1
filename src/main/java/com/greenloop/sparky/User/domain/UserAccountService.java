package com.greenloop.sparky.User.domain;


import com.greenloop.sparky.User.infraestructure.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountService {

    @Autowired
    UserAccountRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<UserAccount> list(){
        return repository.findAll();
    }

    public void save(UserAccount user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }
    
}
