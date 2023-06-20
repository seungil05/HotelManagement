package com.example.hotelmanagement.room;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class RoomConfiguration {
    @Bean
    RoomDao roomDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new RoomDao(namedParameterJdbcTemplate);
    }
}
