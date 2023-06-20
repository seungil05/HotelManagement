package com.example.hotelmanagement.guest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class GuestConfiguration {

    @Bean
    GuestDao guestDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new GuestDao(namedParameterJdbcTemplate);
    }
}
