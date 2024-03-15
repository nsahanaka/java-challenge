package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

	// Use SLF4J logger instead of using System.out.println
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    // Constructor injection for better testability
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Use ResponseEntity to provide more detailed responses, including status codes and headers.
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
    	List<Employee> employees = employeeService.retrieveEmployees();
        if (employees.isEmpty()) {
            // Return 404 NOT FOUND if there are no employees
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Return 200 OK with the list of employees in the response body
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name="employeeId")Long employeeId) {

        if (employeeId < 0) {
        	// Return 400 BAD_REQUEST if employee id is less than 1
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Employee employee = null;

        try {
        	employee = employeeService.getEmployee(employeeId);
            logger.info("Employee was Successfully retrieved");

        } catch (Exception e) {
        	logger.error(e.getMessage());
            // Return a 500 Internal Server Error with a message if something wrong while retrieving the employee
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    	if (employee == null) {
    		// Return 404 NOT FOUND if there is no employee found for the provided Id
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}

    	// Return 200 OK with the matched employee in the response body
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping("/employees")
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody Employee employee) {

        try {
            employeeService.saveEmployee(employee);
            logger.info("Employee Saved Successfully");
            // Return a response entity with HTTP status code 201 Created and the saved employee in the body
            return new ResponseEntity<>(employee, HttpStatus.CREATED);

        } catch (Exception e) {
        	logger.error(e.getMessage());
            // Return a 500 Internal Server Error with a message if something wrong with saving Employee
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving the employee: " + e.getMessage());
        }
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable(name="employeeId")Long employeeId){

        if (employeeId < 0) {
        	// Return 400 BAD_REQUEST if employee id is less than 1
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    	try {
            // Check if the employee exists
            boolean exists = employeeService.existsById(employeeId);
            if (!exists) {
                // If the employee does not exist, return a 404 Not Found
            	logger.error("Employee can not be deleted, the employee was not found in the database");
                return ResponseEntity.notFound().build();
            }

            // If the employee exists, delete it and return a 200 OK response
            employeeService.deleteEmployee(employeeId);
            logger.info("Employee Deleted Successfully");
            return ResponseEntity.ok().body("Employee Deleted Successfully");

        } catch (Exception e) {
        	logger.error(e.getMessage());
            // In case of any exceptions, return a 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting the employee: " + e.getMessage());
        }
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<?> updateEmployee(@Valid @RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){

    	try {
            Employee existingEmployee = employeeService.getEmployee(employeeId);
            if (existingEmployee != null) {
                // Set the ID of the employee to ensure we're updating the correct one
                employee.setId(employeeId);
                employeeService.updateEmployee(employee);
                logger.info("Employee updated Successfully");
                return new ResponseEntity<>(employeeId, HttpStatus.OK);
            } else {
            	// If the employee does not exist, return a 404 Not Found
            	logger.error("Employee can not be updated, the employee was not found in the database");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
        	logger.error(e.getMessage());
        	 // In case of any exceptions, return a 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating the employee: " + e.getMessage());
        }
    }
}
