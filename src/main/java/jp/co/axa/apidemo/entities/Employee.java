package jp.co.axa.apidemo.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name="EMPLOYEE")
public class Employee {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Min(value = 0, message = "Id must be a posittive integer")
    private Long id;

    @Getter
    @Setter
    @NotBlank(message = "Name must not be blank")
    @Column(name="EMPLOYEE_NAME", nullable = false)
    private String name;

    @Getter
    @Setter
    @Min(value = 0, message = "Salary must be a positive integer")
    @Column(name="EMPLOYEE_SALARY")
    private Integer salary;

    @Getter
    @Setter
    @Column(name="DEPARTMENT")
    private String department;

}
