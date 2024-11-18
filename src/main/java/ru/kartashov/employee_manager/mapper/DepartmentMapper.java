package ru.kartashov.employee_manager.mapper;

import ru.kartashov.employee_manager.dto.DepartmentResponse;
import ru.kartashov.employee_manager.entity.Department;

public class DepartmentMapper {
    public static DepartmentResponse parseToResponse (Department department) {
        return new DepartmentResponse(department.getName());
    }
}
