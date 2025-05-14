package com.db_test;

public class Main {
    public static void main(String[] args) {
        EmployeeDAOInterface dao = new EmployeeDAO();

        //Create new Employee
        Employee emp = new Employee(1001, "Ingyin", "Thwe", "x1000", "Ingyin@gmail.com", "1", 1002, "Computer Engineer");
        dao.addEmployee(emp);

        //Read ALL Employees
        dao.getAllEmployee();


        //Update Employee Email
        dao.updateEmployee(1001, "Ingyin@gmail.com");
        System.out.println("After updating");
        dao.printEmployeeTable(dao.getEmployeeById(1001));

        //Delete Employee
        dao.deleteEmployee(1001);
        System.out.println("After Deleting");
        dao.getAllEmployee();

    }
}