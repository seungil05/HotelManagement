package com.example.hotelmanagement;

import com.example.hotelmanagement.employee.EmployeeDao;
import com.example.hotelmanagement.guest.GuestDao;
import com.example.hotelmanagement.room.RoomDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class HotelConfiguration {
    @Bean
    EmployeeDao employeeDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new EmployeeDao(namedParameterJdbcTemplate);
    }

    @Bean
    RoomDao roomDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new RoomDao(namedParameterJdbcTemplate);
    }

    @Bean
    GuestDao guestDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new GuestDao(namedParameterJdbcTemplate);
    }
}
