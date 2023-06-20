package com.example.hotelmanagement.employee;

import ch.ubs.m295.generated.v1.controller.EmployeeApi;
import ch.ubs.m295.generated.v1.dto.Employee;
import ch.ubs.m295.generated.v1.dto.Guest;
import com.example.hotelmanagement.AbstractController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController extends AbstractController implements EmployeeApi {

    private final EmployeeDao employeeDao;

    public EmployeeController(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public ResponseEntity<List<Employee>> employeeGet() {
        Optional<List<Employee>> employees = employeeDao.getEmployees();
        if (employees.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return getRespond(employees.get());
        }

    }

    @Override
    public ResponseEntity<Void> employeeIdDelete(Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        employeeDao.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<Employee>> employeeIdGet(Integer id) {
        Optional<List<Employee>> employee = employeeDao.getEmployeeById(id);
        if (employee.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return getRespond(employee.get());
        }
    }

    @Override
    public ResponseEntity<Void> employeePost(Employee employee) {
        if (employee == null) {
            return ResponseEntity.badRequest().build();
        }
        employeeDao.createEmployee(employee);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> employeePut(Employee employee) {
        if (employee.getEmployeeId() == null) {
            return ResponseEntity.badRequest().build();
        }
        employeeDao.updateEmployee(employee);
        return ResponseEntity.ok().build();
    }
}
