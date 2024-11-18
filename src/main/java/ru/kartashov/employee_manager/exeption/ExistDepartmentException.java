package ru.kartashov.employee_manager.exeption;

public class ExistDepartmentException extends RuntimeException {
    public ExistDepartmentException(String message) {
        super(message);
    }
}
