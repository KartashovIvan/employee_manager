package ru.kartashov.employee_manager.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import ru.kartashov.employee_manager.dto.AddEmployeeRequest;
import ru.kartashov.employee_manager.dto.DepartmentRequest;
import ru.kartashov.employee_manager.dto.DepartmentResponse;
import ru.kartashov.employee_manager.dto.EmployeeResponse;
import ru.kartashov.employee_manager.entity.Department;
import ru.kartashov.employee_manager.entity.Employee;
import ru.kartashov.employee_manager.exeption.DepartmentException;
import ru.kartashov.employee_manager.exeption.EmployeeException;
import ru.kartashov.employee_manager.exeption.ExistDepartmentException;
import ru.kartashov.employee_manager.mapper.DepartmentMapper;
import ru.kartashov.employee_manager.mapper.EmployeeMapper;
import ru.kartashov.employee_manager.repository.DepartmentRepository;
import ru.kartashov.employee_manager.repository.EmployeeRepository;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @CacheEvict(value = "departments", allEntries = true)
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.findByName(request.getName()).isPresent()) {
            throw new ExistDepartmentException("Department %s already exist".formatted(request.getName()));
        }

        Department department = departmentRepository.save(Department.builder()
                .name(request.getName())
                .build());
        return DepartmentMapper.parseToResponse(department);
    }

    @CacheEvict(value = {"departmentEmployees", "employeeByFIO"}, allEntries = true)
    public EmployeeResponse addEmployee(AddEmployeeRequest request) {
        Department department = findDepartmentByName(request.getDepartmentName());
        Employee employee = employeeRepository.findById(request.getIdEmployee())
                .orElseThrow(() -> new EmployeeException("Not exist employee with id: " + request.getIdEmployee()));
        employee.setDepartment(department);
        employeeRepository.flush();
        return EmployeeMapper.parseToResponse(employee);
    }

    @Cacheable(cacheNames = "departmentEmployees")
    public Slice<EmployeeResponse> getDepartmentEmployees(String depName, Integer offset, Integer limit) {
        Department department = findDepartmentByName(depName);
        return employeeRepository.findByDepartmentId(department.getId(), PageRequest.of(offset, limit))
                .map(EmployeeMapper::parseToResponse);
    }

    @Cacheable(cacheNames = "departments")
    public Page<DepartmentResponse> getAllDepartment(Integer offset, Integer limit) {
        return departmentRepository.findAll(PageRequest.of(offset, limit))
                .map(DepartmentMapper::parseToResponse);
    }

    private Department findDepartmentByName (String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new DepartmentException("Not exist department: " + name));
    }
}
