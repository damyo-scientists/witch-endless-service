package witch.endless.service.utils.patch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Patcher {

  private final ObjectMapper mapper;

  public <T> T mergePatch(T target, Object patchValue, Class<T> clazz) {
    try {
      JsonMergePatch mergePatch = JsonMergePatch.fromJson(mapper.valueToTree(patchValue));
      return mergePatch(target, mergePatch, clazz);
    } catch (JsonPatchException e) {
      e.printStackTrace();
      throw new RuntimeException("Error occurred: " + e);
    }
  }

  private <T> T mergePatch(T target, JsonMergePatch mergePatch, Class<T> clazz) {
    try {
      JsonNode node = mapper.convertValue(target, JsonNode.class);
      node = mergePatch.apply(node);
      return mapper.treeToValue(node, clazz);
    } catch (JsonPatchException | JsonProcessingException e) {
      e.printStackTrace();
      throw new RuntimeException("Error occurred: " + e);
    }
  }
}
