package ru.kartashov.employee_manager.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.kartashov.employee_manager.dto.EmployeeDeleteRequest;
import ru.kartashov.employee_manager.dto.EmployeeRequest;
import ru.kartashov.employee_manager.dto.EmployeeResponse;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class EmployeeControllerTest {

    @Test
    @DisplayName("Успешное создание работника")
    public void createTestEmployee() {
        EmployeeRequest newEmployee = getTestEmployee();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(newEmployee)
                .when()
                .post("/employee/create")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Найти работника по имени")
    public void getTestEmployeeByName() {
        EmployeeResponse employee = createEmployee();

        List<EmployeeResponse> employees = RestAssured
                .given()
                .when()
                .get("/employee/employees-by-name?name=" + employee.getName())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList("content", EmployeeResponse.class);

        Assertions.assertFalse(employees.isEmpty());
    }

    @Test
    @DisplayName("Найти несуществующего работника по имени")
    public void getNotExistTestEmployeeByName() {
        List<EmployeeResponse> employees = RestAssured
                .given()
                .when()
                .get("/employee/employees-by-name?name=Ivan")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList("content", EmployeeResponse.class);

        Assertions.assertTrue(employees.isEmpty());
    }

    @Test
    @DisplayName("Удаление работника")
    public void deleteTestEmployee() {
        EmployeeResponse employee = createEmployee();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new EmployeeDeleteRequest(employee.getId()))
                .when()
                .delete("/employee/delete")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());
    }

    @Test
    @DisplayName("Удаление несуществующего работника")
    public void deleteNotExistTestEmployee() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new EmployeeDeleteRequest(1_000_000_000L))
                .when()
                .delete("/employee/delete")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }


    private EmployeeResponse createEmployee() {
        EmployeeRequest newEmployee = getTestEmployee();

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(newEmployee)
                .when()
                .post("/employee/create")
                .then()
                .extract()
                .as(EmployeeResponse.class);
    }

    private EmployeeRequest getTestEmployee() {
        return new EmployeeRequest(UUID.randomUUID() + "_name",
                UUID.randomUUID() + "_surname",
                UUID.randomUUID() + "_patronymic");
    }
}