package com.greenloop.sparky.User.application;

import com.greenloop.sparky.User.domain.UserAccount;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/test")
public class UserController {

    @GetMapping("/myuser")
    public ResponseEntity<String> helloUser(Principal principal) {
        return new ResponseEntity<>("Hello " + principal.getName(), HttpStatus.OK);
    }
}
