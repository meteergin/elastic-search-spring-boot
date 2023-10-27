package com.mergin.elasticsearchspringboot.connector;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.mergin.elasticsearchspringboot.exception.RecordNotFoundException;
import com.mergin.elasticsearchspringboot.model.BaseModel;
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

  public String insertModel(BaseModel model) throws IOException {
    IndexRequest<BaseModel> request =
        IndexRequest.of(i -> i.index(index).id(model.getId()).document(model));
    IndexResponse response = elasticsearchClient.index(request);
    log.info("inserted model info: {}", model);
    return response.result().toString();
  }

  public boolean bulkInsertModels(List<BaseModel> modelList) throws IOException {
    BulkRequest.Builder builder = new BulkRequest.Builder();
    modelList.stream()
        .forEach(
                model ->
                builder.operations(
                    op -> op.index(i -> i.index(index).id(model.getId()).document(model))));
    BulkResponse bulkResponse = elasticsearchClient.bulk(builder.build());
    return !bulkResponse.errors();
  }

  public BaseModel fetchModelById(String id) throws RecordNotFoundException, IOException {
    GetResponse<BaseModel> response =
        elasticsearchClient.get(req -> req.index(index).id(id), BaseModel.class);
    if (!response.found())
      throw new RecordNotFoundException("Model with ID" + id + " not found!");

    return response.source();
  }

  public List<BaseModel> fetchModelsWithMustQuery(BaseModel model) throws IOException {
    List<Query> queries = prepareQueryList(model);
    SearchResponse<BaseModel> modelSearchResponse =
        elasticsearchClient.search(
            req ->
                req.index(index)
                    .query(query -> query.bool(bool -> bool.must(queries))),
                BaseModel.class);

    return modelSearchResponse.hits().hits().stream()
        .map(Hit::source)
        .collect(Collectors.toList());
  }

  public List<BaseModel> fetchModelsWithShouldQuery(BaseModel model) throws IOException {
    List<Query> queries = prepareQueryList(model);
    SearchResponse<BaseModel> response =
        elasticsearchClient.search(
            req ->
                req.index(index)
                    .query(query -> query.bool(bool -> bool.should(queries))),
                BaseModel.class);

    return response.hits().hits().stream()
        .map(Hit::source)
        .collect(Collectors.toList());
  }

  public String deleteModelById(String id) throws IOException {
    DeleteRequest request = DeleteRequest.of(req -> req.index(index).id(id));
    DeleteResponse response = elasticsearchClient.delete(request);
    return response.result().toString();
  }

  public String updateModel(BaseModel model) throws IOException {
    UpdateRequest<BaseModel, BaseModel> updateRequest =
        UpdateRequest.of(
            req -> req.index(index).id(String.valueOf(model.getId())).doc(model));
    UpdateResponse<BaseModel> response = elasticsearchClient.update(updateRequest, BaseModel.class);
    return response.result().toString();
  }

  private List<Query> prepareQueryList(BaseModel model) {
    Map<String, String> conditionMap = new HashMap<>();
//    conditionMap.put("firstName.keyword", employee.getFirstName());
//    conditionMap.put("lastName.keyword", employee.getLastName());
//    conditionMap.put("gender.keyword", employee.getGender());
//    conditionMap.put("email.keyword", employee.getEmail());
//    conditionMap.put("email.country", employee.getCountry());

    return conditionMap.entrySet().stream()
        .filter(entry -> !ObjectUtils.isEmpty(entry.getValue()))
        .map(entry -> QueryBuilderUtils.termQuery(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }
}
