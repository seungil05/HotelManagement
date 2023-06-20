package com.example.hotelmanagement.security;

import ch.ubs.m295.generated.v1.controller.UsersApi;
import ch.ubs.m295.generated.v1.dto.User;
import com.example.hotelmanagement.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class UserController extends AbstractController implements UsersApi {

    @Autowired
    private DataSource dataSource;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public ResponseEntity<Void> usersPost(User user) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        if (!users.userExists(user.getUsername())) {
            UserDetails admin = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(passwordEncoder().encode(user.getPassword()))
                    .roles("USER")
                    .build();
            users.createUser(admin);
        }

        return postRespond();
    }
}
