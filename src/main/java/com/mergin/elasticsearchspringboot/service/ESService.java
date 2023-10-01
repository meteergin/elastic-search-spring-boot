package com.mergin.elasticsearchspringboot.service;

import com.mergin.elasticsearchspringboot.exception.RecordNotFoundException;
import com.mergin.elasticsearchspringboot.model.Employee;
import java.io.IOException;
import java.util.List;

public interface ESService {

  Employee fetchEmployeeById(String id) throws RecordNotFoundException, IOException;

  String insertEmployee(Employee employee) throws IOException;

  boolean bulkInsertEmployees(List<Employee> employees) throws IOException;

  List<Employee> fetchEmployeesWithMustQuery(Employee employee) throws IOException;

  List<Employee> fetchEmployeesWithShouldQuery(Employee employee) throws IOException;

  String deleteEmployeeById(Long id) throws IOException;

  String updateEmployee(Employee employee) throws IOException;
}
