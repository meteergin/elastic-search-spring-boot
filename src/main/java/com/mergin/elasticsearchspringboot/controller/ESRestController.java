package com.mergin.elasticsearchspringboot.controller;

import com.mergin.elasticsearchspringboot.exception.RecordNotFoundException;
import com.mergin.elasticsearchspringboot.model.Employee;
import com.mergin.elasticsearchspringboot.service.ESService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ESRestController {

  private final ESService esService;

  @GetMapping("/index/{id}")
  public ResponseEntity<Employee> fetchEmployeeById(@PathVariable("id") String id)
      throws RecordNotFoundException, IOException {
    Employee employee = esService.fetchEmployeeById(id);
    return ResponseEntity.ok(employee);
  }

  @PostMapping("/index/fetchWithMust")
  public ResponseEntity<List<Employee>> fetchEmployeesWithMustQuery(
      @RequestBody Employee employeeSearchRequest) throws IOException {
    List<Employee> employees = esService.fetchEmployeesWithMustQuery(employeeSearchRequest);
    return ResponseEntity.ok(employees);
  }

  @PostMapping("/index/fetchWithShould")
  public ResponseEntity<List<Employee>> fetchEmployeesWithShouldQuery(
      @RequestBody Employee employeeSearchRequest) throws IOException {
    List<Employee> employees = esService.fetchEmployeesWithShouldQuery(employeeSearchRequest);
    return ResponseEntity.ok(employees);
  }

  @PostMapping("/index")
  public ResponseEntity<String> insertRecords(@RequestBody Employee employee) throws IOException {
    String status = esService.insertEmployee(employee);
    log.info("insert record status: {}", status);
    return ResponseEntity.ok(status);
  }

  @PostMapping("/index/bulk")
  public ResponseEntity<String> bulkInsertEmployees(@RequestBody List<Employee> employees)
      throws IOException {
    boolean isSuccess = esService.bulkInsertEmployees(employees);
    if (isSuccess) {
      return ResponseEntity.ok("Records successfully ingested!");
    } else {
      return ResponseEntity.internalServerError().body("Oops! unable to ingest data");
    }
  }

  @DeleteMapping("/index/{id}")
  public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id) throws IOException {
    String status = esService.deleteEmployeeById(id);
    return ResponseEntity.ok(status);
  }

  @PutMapping("/index")
  public ResponseEntity<String> updateEmployee(@RequestBody Employee employee) throws IOException {
    String status = esService.updateEmployee(employee);
    return ResponseEntity.ok(status);
  }
}
