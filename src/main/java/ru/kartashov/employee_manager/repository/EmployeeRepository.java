package ru.kartashov.employee_manager.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kartashov.employee_manager.entity.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findById (Long id);
    Slice<Employee> findByDepartmentId (Long id, Pageable pageable);
    Slice<Employee> findByNameOrSurnameOrPatronymic(String name, String surname, String patronymic, Pageable pageable);
}
