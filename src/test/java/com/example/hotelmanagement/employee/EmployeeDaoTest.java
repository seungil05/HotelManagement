package com.example.hotelmanagement.employee;

import ch.ubs.m295.generated.v1.dto.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void getEmployees(){
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        Employee employee2 = new Employee()
                .employeeId(2)
                .firstName("Max")
                .lastName("Mustermann")
                .job("Receptionist");

        List<Employee> expectedEmployees = Arrays.asList(employee, employee2);

        when(namedParameterJdbcTemplate.query(
                eq("SELECT * FROM Employees"),
                argumentCaptor.capture(),
                any(RowMapper.class)
        )).thenReturn(expectedEmployees);

        // Act
        Optional<List<Employee>> employees = employeeDao.getEmployees();

        // Assert
        assertEquals(expectedEmployees, employees.get());
    }

    @Test
    void getEmployeesById() {
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        when(namedParameterJdbcTemplate.query(
                eq("SELECT * FROM Employees WHERE employeeId = :employeeId"),
                argumentCaptor.capture(),
                any(RowMapper.class)
        )).thenReturn(Arrays.asList(employee));

        // Act
        Optional<List<Employee>> employeeById = employeeDao.getEmployeeById(1);

        // Assert
        assertEquals(employee, employeeById.get().get(0));
    }

    @Test
    void updateEmployee() {
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        when(namedParameterJdbcTemplate.update(
                eq("UPDATE Employees SET firstName = :firstName, lastName = :lastName, job = :job WHERE employeeId = :employeeId"),
                argumentCaptor.capture()
        )).thenReturn(1);

        // Act
        employeeDao.updateEmployee(employee);

        // Assert
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("firstName")).isEqualTo("John");
    }

    @Test
    void deleteEmployee(){
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        when(namedParameterJdbcTemplate.update(
                eq("DELETE FROM Employees WHERE employeeId = :employeeId"),
                argumentCaptor.capture()
        )).thenReturn(1);

        // Act
        employeeDao.deleteEmployee(1);

        // Assert
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("employeeId")).isEqualTo(1);
    }
}