package com.mergin.elasticsearchspringboot.connector;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.mergin.elasticsearchspringboot.exception.RecordNotFoundException;
import com.mergin.elasticsearchspringboot.model.Employee;
import com.mergin.elasticsearchspringboot.utils.QueryBuilderUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class ESClientConnector {

  private final ElasticsearchClient elasticsearchClient;

  @Value("${elastic-search.index}")
  private String index;

  public String insertEmployee(Employee employee) throws IOException {
    IndexRequest<Employee> request =
        IndexRequest.of(i -> i.index(index).id(employee.getId()).document(employee));
    IndexResponse response = elasticsearchClient.index(request);
    log.info("inserted employee info: {}", employee);
    return response.result().toString();
  }

  public boolean bulkInsertEmployees(List<Employee> employeeList) throws IOException {
    BulkRequest.Builder builder = new BulkRequest.Builder();
    employeeList.stream()
        .forEach(
            employee ->
                builder.operations(
                    op -> op.index(i -> i.index(index).id(employee.getId()).document(employee))));
    BulkResponse bulkResponse = elasticsearchClient.bulk(builder.build());
    return !bulkResponse.errors();
  }

  public Employee fetchEmployeeById(String id) throws RecordNotFoundException, IOException {
    GetResponse<Employee> response =
        elasticsearchClient.get(req -> req.index(index).id(id), Employee.class);
    if (!response.found())
      throw new RecordNotFoundException("Employee with ID" + id + " not found!");

    return response.source();
  }

  public List<Employee> fetchEmployeesWithMustQuery(Employee employee) throws IOException {
    List<Query> queries = prepareQueryList(employee);
    SearchResponse<Employee> employeeSearchResponse =
        elasticsearchClient.search(
            req ->
                req.index(index)
                    .size(employee.getSize())
                    .query(query -> query.bool(bool -> bool.must(queries))),
            Employee.class);

    return employeeSearchResponse.hits().hits().stream()
        .map(Hit::source)
        .collect(Collectors.toList());
  }

  public List<Employee> fetchEmployeesWithShouldQuery(Employee employee) throws IOException {
    List<Query> queries = prepareQueryList(employee);
    SearchResponse<Employee> employeeSearchResponse =
        elasticsearchClient.search(
            req ->
                req.index(index)
                    .size(employee.getSize())
                    .query(query -> query.bool(bool -> bool.should(queries))),
            Employee.class);

    return employeeSearchResponse.hits().hits().stream()
        .map(Hit::source)
        .collect(Collectors.toList());
  }

  public String deleteEmployeeById(Long id) throws IOException {
    DeleteRequest request = DeleteRequest.of(req -> req.index(index).id(String.valueOf(id)));
    DeleteResponse response = elasticsearchClient.delete(request);
    return response.result().toString();
  }

  public String updateEmployee(Employee employee) throws IOException {
    UpdateRequest<Employee, Employee> updateRequest =
        UpdateRequest.of(
            req -> req.index(index).id(String.valueOf(employee.getId())).doc(employee));
    UpdateResponse<Employee> response = elasticsearchClient.update(updateRequest, Employee.class);
    return response.result().toString();
  }

  private List<Query> prepareQueryList(Employee employee) {
    Map<String, String> conditionMap = new HashMap<>();
    conditionMap.put("firstName.keyword", employee.getFirstName());
    conditionMap.put("lastName.keyword", employee.getLastName());
    conditionMap.put("gender.keyword", employee.getGender());
    conditionMap.put("email.keyword", employee.getEmail());
    conditionMap.put("email.country", employee.getCountry());

    return conditionMap.entrySet().stream()
        .filter(entry -> !ObjectUtils.isEmpty(entry.getValue()))
        .map(entry -> QueryBuilderUtils.termQuery(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }
}
