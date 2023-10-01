package com.mergin.elasticsearchspringboot.service.impl;

import com.mergin.elasticsearchspringboot.connector.ESClientConnector;
import com.mergin.elasticsearchspringboot.exception.RecordNotFoundException;
import com.mergin.elasticsearchspringboot.model.Employee;
import com.mergin.elasticsearchspringboot.service.ESService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ESServiceImpl implements ESService {

  private final ESClientConnector esClientConnector;

  @Override
  public Employee fetchEmployeeById(String id) throws RecordNotFoundException, IOException {
    return esClientConnector.fetchEmployeeById(id);
  }

  @Override
  public String insertEmployee(Employee employee) throws IOException {
    return esClientConnector.insertEmployee(employee);
  }

  @Override
  public boolean bulkInsertEmployees(List<Employee> employees) throws IOException {
    return esClientConnector.bulkInsertEmployees(employees);
  }

  @Override
  public List<Employee> fetchEmployeesWithMustQuery(Employee employee) throws IOException {
    return esClientConnector.fetchEmployeesWithMustQuery(employee);
  }

  @Override
  public List<Employee> fetchEmployeesWithShouldQuery(Employee employee) throws IOException {
    return esClientConnector.fetchEmployeesWithShouldQuery(employee);
  }

  @Override
  public String deleteEmployeeById(Long id) throws IOException {
    return esClientConnector.deleteEmployeeById(id);
  }

  @Override
  public String updateEmployee(Employee employee) throws IOException {
    return esClientConnector.updateEmployee(employee);
  }
}
