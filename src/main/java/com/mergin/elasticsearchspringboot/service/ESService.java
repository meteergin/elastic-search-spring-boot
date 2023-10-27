package com.mergin.elasticsearchspringboot.service;

import com.mergin.elasticsearchspringboot.exception.RecordNotFoundException;
import com.mergin.elasticsearchspringboot.model.BaseModel;
import java.io.IOException;
import java.util.List;

public interface ESService {

  BaseModel fetchModelById(String id) throws RecordNotFoundException, IOException;

  String insertModel(BaseModel model) throws IOException;

  boolean bulkInsertModels(List<BaseModel> models) throws IOException;

  List<BaseModel> fetchModelsWithMustQuery(BaseModel model) throws IOException;

  List<BaseModel> fetchModelsWithShouldQuery(BaseModel model) throws IOException;

  String deleteModelById(String id) throws IOException;

  String updateModel(BaseModel model) throws IOException;
}
