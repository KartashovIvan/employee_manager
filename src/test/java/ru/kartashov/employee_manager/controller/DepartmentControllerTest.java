package ru.kartashov.employee_manager.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.kartashov.employee_manager.dto.*;

import java.util.List;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DepartmentControllerTest {

    @Test
    @DisplayName("Успешное создание отдела")
    public void createTestDepartment() {
        DepartmentRequest test_dep = getTestDepartment();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(test_dep)
                .when()
                .post("/department/create")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Создание существующего отдела")
    public void createExistDepartment() {
        DepartmentResponse existDepartment = createDepartment();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new DepartmentRequest(existDepartment.getName()))
                .when()
                .post("/department/create")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("Добавление работника в отдел")
    public void addEmployeeToDepartment() {
        DepartmentResponse department = createDepartment();
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new AddEmployeeRequest(department.getName(), 0L))
                .when()
                .post("/department/add-employee")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());
    }

    @Test
    @DisplayName("Получение сотрудников отдела")
    public void getDepartmentEmployees() {
        DepartmentResponse department = createDepartment();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new AddEmployeeRequest(department.getName(), 0L))
                .when()
                .post("/department/add-employee");

        List<EmployeeResponse> employees = RestAssured
                .given()
                .when()
                .get("department/get-employees/"+department.getName())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList("content", EmployeeResponse.class);

        Assertions.assertFalse(employees.isEmpty());
    }

    @Test
    @DisplayName("Получение всех отдела")
    public void getAllDepartment() {
        createDepartment();

        List<DepartmentResponse> departments = RestAssured
                .given()
                .when()
                .get("department/find-all")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList("content", DepartmentResponse.class);

        Assertions.assertFalse(departments.isEmpty());
    }

    private DepartmentResponse createDepartment() {
        DepartmentRequest test_dep = getTestDepartment();

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(test_dep)
                .when()
                .post("/department/create")
                .then()
                .extract()
                .as(DepartmentResponse.class);
    }

    private DepartmentRequest getTestDepartment() {
        return new DepartmentRequest(UUID.randomUUID().toString());
    }
}