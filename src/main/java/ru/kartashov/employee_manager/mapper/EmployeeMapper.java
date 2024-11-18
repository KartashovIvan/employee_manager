package ru.kartashov.employee_manager.mapper;

import ru.kartashov.employee_manager.dto.DepartmentResponse;
import ru.kartashov.employee_manager.dto.EmployeeResponse;
import ru.kartashov.employee_manager.entity.Employee;

import java.util.Objects;

public class EmployeeMapper {
    public static EmployeeResponse parseToResponse(Employee employee) {
        EmployeeResponse response = EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .patronymic(employee.getPatronymic())
                .build();

        if (Objects.nonNull(employee.getDepartment())) {
            response.setDepartment(new DepartmentResponse(employee.getDepartment().getName()));
        }

        return response;
    }
}
