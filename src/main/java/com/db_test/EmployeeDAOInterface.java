package com.db_test;

import java.util.List;

public interface EmployeeDAOInterface {
    void addEmployee(Employee emp);

    Employee getEmployeeById(int employeeNumber);

    void updateEmployee(int employeeNumber, String newEmail);

    void deleteEmployee(int employeeNumber);

    void printEmployeeTable(Employee emp);

    List<Employee> getAllEmployee();
}
