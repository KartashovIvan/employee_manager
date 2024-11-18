package ru.kartashov.employee_manager.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import ru.kartashov.employee_manager.dto.EmployeeDeleteRequest;
import ru.kartashov.employee_manager.dto.EmployeeRequest;
import ru.kartashov.employee_manager.dto.EmployeeResponse;
import ru.kartashov.employee_manager.entity.Employee;
import ru.kartashov.employee_manager.exeption.EmployeeException;
import ru.kartashov.employee_manager.mapper.EmployeeMapper;
import ru.kartashov.employee_manager.repository.EmployeeRepository;

@Service
@Data
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @CacheEvict(value = "employeeByFIO", allEntries = true)
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee = employeeRepository.save(Employee.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .build());
        return EmployeeMapper.parseToResponse(employee);
    }

    @CacheEvict(value = {"employeeByFIO", "departmentEmployees"}, allEntries = true)
    public EmployeeResponse deleteEmployee(EmployeeDeleteRequest request) {
        Employee employee = employeeRepository.findById(request.getId())
                .orElseThrow(() -> new EmployeeException("Not exists employee with id: " + request.getId()));
        employeeRepository.delete(employee);

        return EmployeeMapper.parseToResponse(employee);
    }

    @Cacheable(cacheNames = "employeeByFIO")
    public Slice<EmployeeResponse> getEmployeeByNameOrSurnameOrPatronymic(EmployeeRequest request, Integer offset, Integer limit) {
        return employeeRepository.findByNameOrSurnameOrPatronymic(request.getName(),
                        request.getSurname(), request.getPatronymic(), PageRequest.of(offset, limit))
                .map(EmployeeMapper::parseToResponse);
    }
}
