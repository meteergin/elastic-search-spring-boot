package com.mergin.elasticsearchspringboot.controller;

import com.mergin.elasticsearchspringboot.exception.RecordNotFoundException;
import com.mergin.elasticsearchspringboot.model.BaseModel;
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
  public ResponseEntity<BaseModel> fetchModelById(@PathVariable("id") String id)
      throws RecordNotFoundException, IOException {
    BaseModel model = esService.fetchModelById(id);
    return ResponseEntity.ok(model);
  }

  @PostMapping("/index/fetchWithMust")
  public ResponseEntity<List<BaseModel>> fetchModelsWithMustQuery(
      @RequestBody BaseModel modelSearchRequest) throws IOException {
    List<BaseModel> models = esService.fetchModelsWithMustQuery(modelSearchRequest);
    return ResponseEntity.ok(models);
  }

  @PostMapping("/index/fetchWithShould")
  public ResponseEntity<List<BaseModel>> fetchModelsWithShouldQuery(
      @RequestBody BaseModel modelSearchRequest) throws IOException {
    List<BaseModel> models = esService.fetchModelsWithShouldQuery(modelSearchRequest);
    return ResponseEntity.ok(models);
  }

  @PostMapping("/index")
  public ResponseEntity<String> insertRecords(@RequestBody BaseModel model) throws IOException {
    String status = esService.insertModel(model);
    log.info("insert record status: {}", status);
    return ResponseEntity.ok(status);
  }

  @PostMapping("/index/bulk")
  public ResponseEntity<String> bulkInsertModels(@RequestBody List<BaseModel> models)
      throws IOException {
    boolean isSuccess = esService.bulkInsertModels(models);
    if (isSuccess) {
      return ResponseEntity.ok("Records successfully ingested!");
    } else {
      return ResponseEntity.internalServerError().body("Oops! unable to ingest data");
    }
  }

  @DeleteMapping("/index/{id}")
  public ResponseEntity<String> deleteModel(@PathVariable("id") String id) throws IOException {
    String status = esService.deleteModelById(id);
    return ResponseEntity.ok(status);
  }

  @PutMapping("/index")
  public ResponseEntity<String> updateModel(@RequestBody BaseModel model) throws IOException {
    String status = esService.updateModel(model);
    return ResponseEntity.ok(status);
  }
}
