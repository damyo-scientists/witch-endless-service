package witch.endless.service.response.auth;

import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtResponse implements Serializable {

  @JsonValue
  private String token;
}
