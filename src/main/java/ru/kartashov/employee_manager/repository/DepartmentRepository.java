package ru.kartashov.employee_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kartashov.employee_manager.entity.Department;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByName(String name);
}
