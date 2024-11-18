package ru.kartashov.employee_manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kartashov.employee_manager.dto.AddEmployeeRequest;
import ru.kartashov.employee_manager.dto.DepartmentRequest;
import ru.kartashov.employee_manager.dto.DepartmentResponse;
import ru.kartashov.employee_manager.dto.EmployeeResponse;
import ru.kartashov.employee_manager.service.DepartmentService;

@RestController
@RequestMapping("/department")
@AllArgsConstructor
@Tag(name = "Контроллер отдела" , description = "Операции с отделами")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание нового отдела")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DepartmentResponse.class)))),
            @ApiResponse(responseCode = "409",
                    content = @Content(mediaType = "application/text",
                            schema = @Schema(implementation = String.class)))})
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody DepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(request));
    }

    @PostMapping("/add-employee")
    @Operation(summary = "Добавить работника в отдел")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class)))),
            @ApiResponse(responseCode = "404",
                    content = @Content(mediaType = "application/text",
                            schema = @Schema(implementation = String.class)))})
    public ResponseEntity<EmployeeResponse> addEmployee(@RequestBody AddEmployeeRequest request) {
        return ResponseEntity.accepted().body(departmentService.addEmployee(request));
    }

    @GetMapping("/get-employees/{departmentName}")
    @Operation(summary = "Получить работников по отделам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class))))})
    public ResponseEntity<Slice<EmployeeResponse>> getDepartmentEmployees(@PathVariable String departmentName,
                                                                          @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                                          @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return ResponseEntity.ok().body(departmentService.getDepartmentEmployees(departmentName, offset, limit));
    }

    @GetMapping("/find-all")
    @Operation(summary = "Получить все отделы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class))))})
    public ResponseEntity<Page<DepartmentResponse>> getAllDepartment(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                                     @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return ResponseEntity.ok().body(departmentService.getAllDepartment(offset, limit));
    }
}
