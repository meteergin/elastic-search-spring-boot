package com.mergin.elasticsearchspringboot.service.impl;

import com.mergin.elasticsearchspringboot.connector.ESClientConnector;
import com.mergin.elasticsearchspringboot.exception.RecordNotFoundException;
import com.mergin.elasticsearchspringboot.model.BaseModel;
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
  public BaseModel fetchModelById(String id) throws RecordNotFoundException, IOException {
    return esClientConnector.fetchModelById(id);
  }

  @Override
  public String insertModel(BaseModel model) throws IOException {
    return esClientConnector.insertModel(model);
  }

  @Override
  public boolean bulkInsertModels(List<BaseModel> models) throws IOException {
    return esClientConnector.bulkInsertModels(models);
  }

  @Override
  public List<BaseModel> fetchModelsWithMustQuery(BaseModel model) throws IOException {
    return esClientConnector.fetchModelsWithMustQuery(model);
  }

  @Override
  public List<BaseModel> fetchModelsWithShouldQuery(BaseModel model) throws IOException {
    return esClientConnector.fetchModelsWithShouldQuery(model);
  }

  @Override
  public String deleteModelById(String id) throws IOException {
    return esClientConnector.deleteModelById(id);
  }

  @Override
  public String updateModel(BaseModel model) throws IOException {
    return esClientConnector.updateModel(model);
  }
}
