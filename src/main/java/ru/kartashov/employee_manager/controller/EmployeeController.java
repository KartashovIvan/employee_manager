package ru.kartashov.employee_manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kartashov.employee_manager.dto.EmployeeDeleteRequest;
import ru.kartashov.employee_manager.dto.EmployeeRequest;
import ru.kartashov.employee_manager.dto.EmployeeResponse;
import ru.kartashov.employee_manager.service.EmployeeService;

@RestController
@RequestMapping("/employee")
@AllArgsConstructor
@Tag(name = "Контроллер работников" , description = "Операции с работниками")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание нового работника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class))))})
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(request));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Удаление работника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class)))),
            @ApiResponse(responseCode = "404",
                    content = @Content(mediaType = "application/text",
                            schema = @Schema(implementation = String.class)))})
    public ResponseEntity<EmployeeResponse> deleteEmployee(@RequestBody EmployeeDeleteRequest request) {
        return ResponseEntity.accepted().body(employeeService.deleteEmployee(request));
    }

    @GetMapping("/employees-by-name")
    @Operation(summary = "Получить работника по ФИО")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class))))})
    public ResponseEntity<Slice<EmployeeResponse>> getEmployeeByNameOrSurnameOrPatronymic(@RequestParam(required = false) String name,
                                                                                          @RequestParam(required = false) String surname,
                                                                                          @RequestParam(required = false) String patronymic,
                                                                                          @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                                                          @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        EmployeeRequest request = EmployeeRequest.builder().name(name).surname(surname).patronymic(patronymic).build();
        return ResponseEntity.ok().body(employeeService.getEmployeeByNameOrSurnameOrPatronymic(request, offset, limit));
    }
}
