package ru.kartashov.employee_manager.exeption;

public class EmployeeException extends RuntimeException{
    public EmployeeException() {
    }

    public EmployeeException(String message) {
        super(message);
    }
}
