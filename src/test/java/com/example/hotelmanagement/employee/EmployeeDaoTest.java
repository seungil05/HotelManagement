package com.example.hotelmanagement.employee;

import ch.ubs.m295.generated.v1.dto.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeDaoTest {
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private EmployeeDao employeeDao;

    Employee employee = new Employee()
            .employeeId(1)
            .firstName("John")
            .lastName("Doe")
            .job("Receptionist");

    @BeforeEach
    void setUp() {
        this.employeeDao = new EmployeeDao(this.namedParameterJdbcTemplate);
    }

    @Test
    void insertEmployee(){
        //Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        //Act
        employeeDao.insertEmployee(employee);

        //Assert
        verify(this.namedParameterJdbcTemplate).update(
                eq("""
                INSERT INTO Employees (firstName, lastName, job) VALUES (:firstName, :lastName, :job)"""),
                argumentCaptor.capture(),
                any(GeneratedKeyHolder.class)
        );
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("firstName")).isEqualTo("John");
        assertThat(mapSqlParameterSource.getValue("lastName")).isEqualTo("Doe");
    }

    @Test
    void updateEmployee() {
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        // Act
        employeeDao.updateEmployee(employee);

        verify(namedParameterJdbcTemplate, times(1)).update(
                eq("UPDATE Employees SET firstName = :firstName, lastName = :lastName, job = :job WHERE employeeId = :employeeId"),
                argumentCaptor.capture()
        );
        // Assert
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("firstName")).isEqualTo("John");
    }

    @Test
    void deleteEmployee(){
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        // Act
        employeeDao.deleteEmployee(1);
        verify(namedParameterJdbcTemplate, times(1)).update(
                eq("DELETE FROM Employees WHERE employeeId = :employeeId"),
                argumentCaptor.capture()
        );
        // Assert
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("employeeId")).isEqualTo(1);
    }
}