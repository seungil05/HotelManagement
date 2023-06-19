package com.example.hotelmanagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuestConfiguration {

    @Bean
    GuestController guestController(GuestDao guestDao) {
        return new GuestController(guestDao);
    }
}
