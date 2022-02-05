package witch.endless.service.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SignInRequest {

  @NotNull
  @Schema(description = "[Account] firebaseJwt")
  private String firebaseJwt;
}
