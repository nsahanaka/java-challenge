package jp.co.axa.apidemo;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApiDemoApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    // Employee service
    @MockBean
    private EmployeeService employeeService;


    @Autowired
    private ObjectMapper objectMapper;

    // This test verifies if the retrieveEmployees() method returns an Employee list.
    @Test
    public void getEmployeesShouldReturnEmployeeList() throws Exception {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Nanayakkara Sahanaka");
        employee.setSalary(1000000);
        employee.setDepartment("R&D");

        when(employeeService.retrieveEmployees()).thenReturn(Arrays.asList(employee));

        mockMvc.perform(get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(employee.getName())));

        verify(employeeService, times(1)).retrieveEmployees();
    }

    // This test verifies if an invalid name for the Employee doesn't succeed.
    @Test
    public void saveEmployeeValidationFail() throws Exception {
    	// Assuming this object has validation errors
        Employee invalidEmployee = new Employee();
        // Example of invalid field
        invalidEmployee.setName("");

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).saveEmployee(any(Employee.class));
    }

    // TODO: Add more test cases.
    // More test cases should be added to increase the integrity of the business logic.
    // I stopped adding more test cases due to time constraints.
}
