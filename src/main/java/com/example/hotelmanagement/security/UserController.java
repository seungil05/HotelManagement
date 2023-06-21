package com.example.hotelmanagement.security;

import ch.ubs.m295.generated.v1.controller.UsersApi;
import ch.ubs.m295.generated.v1.dto.User;
import com.example.hotelmanagement.AbstractController;
import com.example.hotelmanagement.room.RoomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(RoomController.class);

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
            logger.info("User created: " + user.getUsername());
        }

        return postRespond();
    }

    @Override
    public ResponseEntity<Void> usersPut(User user) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        if (users.userExists(user.getUsername()) && !user.getPassword().isEmpty()) {
            users.deleteUser(user.getUsername());
            users.createUser(org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(passwordEncoder().encode(user.getNewPassword()))
                    .roles("USER")
                    .build());
            logger.info("Password changed for user: " + user.getUsername());
        }
        return putRespond();
    }

    @Override
    public ResponseEntity<Void> usersUsernameDelete(String username) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        if (users.userExists(username)) {
            users.deleteUser(username);
            logger.info("User deleted: " + username);
        }
        return deleteRespond();
    }
}
