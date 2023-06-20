package com.example.hotelmanagement.employee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class EmployeeConfiguration {

    @Bean
    EmployeeDao employeeDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new EmployeeDao(namedParameterJdbcTemplate);
    }
}
