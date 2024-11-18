package ru.kartashov.employee_manager.exeption;

public class DepartmentException extends RuntimeException {
    public DepartmentException() {
    }

    public DepartmentException(String message) {
        super(message);
    }
}
