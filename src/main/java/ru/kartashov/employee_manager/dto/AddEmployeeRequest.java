package ru.kartashov.employee_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddEmployeeRequest {
    private String departmentName;
    private Long idEmployee;
}
