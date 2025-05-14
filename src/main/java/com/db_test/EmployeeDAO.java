package com.db_test;



import de.vandermeer.asciitable.AsciiTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO implements EmployeeDAOInterface {

    //Create Employee
    public void addEmployee(Employee emp) {
        String sql = "INSERT INTO employees VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DBconfig.URL, DBconfig.USER, DBconfig.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emp.getEmployeeNumber());
            stmt.setString(2, emp.getLastName());
            stmt.setString(3, emp.getFirstName());
            stmt.setString(4, emp.getExtension());
            stmt.setString(5, emp.getEmail());
            stmt.setString(6, emp.getOfficeCode());
            if (emp.getReportsTo() != null) {
                stmt.setInt(7, emp.getReportsTo());
            } else
                stmt.setNull(7, Types.INTEGER);
            stmt.setString(8, emp.getJobTitle());

            stmt.executeUpdate();
            System.out.println("Employee added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Read Employee
    public List<Employee> getAllEmployee() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (Connection connection = DriverManager.getConnection(DBconfig.URL, DBconfig.USER, DBconfig.PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            //Header
            String format = "| %-5s | %-15s | %-15s | %-10s | %-25s | %-10s | %-10s | %-15s |%n";
            System.out.format("+-------+-----------------+-----------------+------------+---------------------------+------------+------------+-----------------+%n");
            System.out.format("|  ID   | Last Name       | First Name      | Extension  | Email                     | OfficeCode | ReportsTo  | Job Title       |%n");
            System.out.format("+-------+-----------------+-----------------+------------+---------------------------+------------+------------+-----------------+%n");

            while (rs.next()) {
                int employeeNumber = rs.getInt("employeeNumber");
                String lastName = rs.getString("lastName");
                String firstName = rs.getString("firstName");
                String extension = rs.getString("extension");
                String email = rs.getString("email");
                String officeCode = rs.getString("officeCode");
                Integer reportsTo = rs.getObject("reportsTo") != null ? rs.getInt("reportsTo") : null;
                String jobTitle = rs.getString("jobTitle");


                //Print each row
                System.out.format(format, employeeNumber, lastName, firstName,
                        extension, email, officeCode,
                        reportsTo != null ? reportsTo.toString() : "NULL", jobTitle);

                //Add to list
                Employee emp = new Employee(employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle);
                employees.add(emp);
            }
            System.out.format("+-------+-----------------+-----------------+------------+---------------------------+------------+------------+-----------------+%n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    //Update Employee Email
    public void updateEmployee(int employeeNumber, String newEmail) {
        String sql = "UPDATE employees SET email = ? WHERE employeeNumber = ?";
        try (Connection conn = DriverManager.getConnection(DBconfig.URL, DBconfig.USER, DBconfig.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newEmail);
            stmt.setInt(2, employeeNumber);
            stmt.executeUpdate();
            System.out.println("Email Updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Show the Updated Employee Result
    public void printEmployeeTable(Employee emp) {
        if (emp == null) {
            System.out.println("Employee Not Found");
            return;
        }
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("EmployeeNumber", "First Name", "Last Name",
                "Extension", "Email", "Office Code",
                "Report To", "Job Title");
        at.addRule();
        at.addRow(
                emp.getEmployeeNumber(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getExtension(),
                emp.getEmail(),
                emp.getOfficeCode(),
                emp.getReportsTo(),
                emp.getJobTitle()
        );
        at.addRule();
        System.out.println(at.render());
    }

    //Delete Employee by their number
    public void deleteEmployee(int employeeNumber) {
        String sql = "DELETE FROM employees WHERE employeeNumber = ?";
        try (Connection conn = DriverManager.getConnection(DBconfig.URL, DBconfig.USER, DBconfig.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeNumber);
            int rowAffected = stmt.executeUpdate();
            if (rowAffected > 0)
                System.out.println("Employee Number " + employeeNumber + "is deleted.");
            else
                System.out.println("No employee found with Employee Number " + employeeNumber + ".");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Employee getEmployeeById(int employeeNumber) {
        String sql = "SELECT * FROM employees WHERE employeeNumber = ?";

        try (Connection conn = DriverManager.getConnection(DBconfig.URL, DBconfig.USER, DBconfig.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int empNum = rs.getInt("employeeNumber");
                String lastName = rs.getString("lastName");
                String firstName = rs.getString("firstName");
                String extension = rs.getString("extension");
                String email = rs.getString("email");
                String officeCode = rs.getString("officeCode");
                Integer reportsTo = rs.getObject("reportsTo") != null ? rs.getInt("reportsTo") : null;
                String jobTitle = rs.getString("jobTitle");

                return new Employee(empNum, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // No employee found
    }

}
