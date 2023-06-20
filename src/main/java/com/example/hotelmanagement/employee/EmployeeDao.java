package com.example.hotelmanagement.employee;

import ch.ubs.m295.generated.v1.dto.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Optional;

public class EmployeeDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String SELECT_ALL_QUERY = "SELECT * FROM Employees";

    private final static String UPDATE_EMPLOYEE_QUERY = "UPDATE Employees SET firstName = :firstName, lastName = :lastName, job = :job WHERE employeeId = :employeeId";

    private final static String SELECT_BY_ID_QUERY = "SELECT * FROM Employees WHERE employeeId = :employeeId";

    private final static String DELETE_EMPLOYEE_QUERY = "DELETE FROM Employees WHERE employeeId = :employeeId";

    private final static String CREATE_EMPLOYEE_QUERY = "INSERT INTO Employees (firstName, lastName, job) VALUES (:firstName, :lastName, :job)";
    public EmployeeDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<List<Employee>> getEmployees(){
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        List<Employee> result = namedParameterJdbcTemplate.query(
                SELECT_ALL_QUERY,
                namedParameters,
                (rs, rowNum) ->{
                    int id = rs.getInt("employeeId");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    String job = rs.getString("job");
                    return new Employee()
                            .employeeId(id)
                            .firstName(firstName)
                            .lastName(lastName)
                            .job(job);
                }
        );
        if (result.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }

    public Optional<List<Employee>> getEmployeeById(int employeeId){
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("employeeId", employeeId);
        List<Employee> result = namedParameterJdbcTemplate.query(
                SELECT_BY_ID_QUERY,
                namedParameters,
                (rs, rowNum) -> new Employee()
                        .employeeId(rs.getInt("employeeId"))
                        .firstName(rs.getString("firstName"))
                        .lastName(rs.getString("lastName"))
                        .job(rs.getString("job"))
        );
        if (result.size() > 1) {
            throw new RuntimeException("More than one room with id " + employeeId);
        }
        if (result.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }

    public ResponseEntity<Void> updateEmployee(Employee employee){
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("employeeId", employee.getEmployeeId())
                .addValue("firstName", employee.getFirstName())
                .addValue("lastName", employee.getLastName())
                .addValue("job", employee.getJob());
        namedParameterJdbcTemplate.update(
                UPDATE_EMPLOYEE_QUERY,
                namedParameters
        );
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> createEmployee(Employee employee){
        KeyHolder keyHolder = new GeneratedKeyHolder();

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("firstName", employee.getFirstName())
                .addValue("lastName", employee.getLastName())
                .addValue("job", employee.getJob());

        namedParameterJdbcTemplate.update(
                CREATE_EMPLOYEE_QUERY,
                namedParameters,
                keyHolder
        );
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteEmployee(int employeeId){
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("employeeId", employeeId);
        namedParameterJdbcTemplate.update(
                DELETE_EMPLOYEE_QUERY,
                namedParameters
        );
        return ResponseEntity.ok().build();
    }
}
