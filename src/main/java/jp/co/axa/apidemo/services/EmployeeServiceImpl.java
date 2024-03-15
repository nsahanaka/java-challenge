package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

// Interface implementation of the Employee service
@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    // Constructor injection ensures immutability and prevents the possibility of the dependency being accidentally replaced after instantiation.
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Finds all employees in the database
    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    // Finds employee with the id
    public Employee getEmployee(Long employeeId) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        return optEmp.get();
    }

    // Insert data employees into the database
    @Transactional
    public void saveEmployee(Employee employee){
        employeeRepository.save(employee);
    }

    // Deletes employee
    @Transactional
    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }

    // Update employee in the database
    @Transactional
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    // Check whether the employee exists in the database with the corresponding ID
    public boolean existsById(Long id) {
        return employeeRepository.existsById(id);
    }
}