package com.example.hotelmanagement.employee;

import ch.ubs.m295.generated.v1.controller.EmployeeApi;
import ch.ubs.m295.generated.v1.dto.Employee;
import com.example.hotelmanagement.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController extends AbstractController implements EmployeeApi {

    private final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeDao employeeDao;

    public EmployeeController(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public ResponseEntity<List<Employee>> employeeGet() {
        Optional<List<Employee>> employees = employeeDao.getEmployees();
        if (employees.isEmpty()){
            logger.error("No employees found");
            return notFoundRespond();
        } else {
            logger.info("All Employees selected");
            return getRespond(employees.get());
        }

    }

    @Override
    public ResponseEntity<Void> employeeIdDelete(Integer id) {
        if (id == null) {
            return badRespond();
        }
        try {
            logger.info("Employee with id {} deleted", id);
            employeeDao.deleteEmployee(id);
            return deleteRespond();
        }
        catch (Exception e) {
            logger.error("Employee with id {} not found", id);
            return notFoundRespond();
        }
    }

    @Override
    public ResponseEntity<List<Employee>> employeeIdGet(Integer id) {
        Optional<List<Employee>> employee = employeeDao.getEmployeeById(id);
        if (employee.isEmpty()){
            logger.error("No employees found");
            return notFoundRespond();
        } else {
            logger.info("Employee with matching Id found");
            return getRespond(employee.get());
        }
    }

    @Override
    public ResponseEntity<Void> employeePost(Employee employee) {
        if (employee == null) {
            return badRespond();
        }
        try {
            logger.info("Employee created");
            employeeDao.createEmployee(employee);
            return postRespond();
        }
        catch (Exception e) {
            logger.error("Employee could not Be inserted");
            return notFoundRespond();
        }
    }

    @Override
    public ResponseEntity<Void> employeePut(Employee employee) {
        if (employee.getEmployeeId() == null) {
            return badRespond();
        }
        try {
            logger.info("Employee updated");
            employeeDao.updateEmployee(employee);
            return putRespond();
        }
        catch (Exception e) {
            logger.error("Employee could not Be updated");
            return notFoundRespond();
        }
    }
}
