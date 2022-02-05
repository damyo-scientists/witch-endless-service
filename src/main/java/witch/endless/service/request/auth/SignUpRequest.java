package witch.endless.service.request.auth;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import witch.endless.service.model.account.Account;
import witch.endless.service.model.account.deviceToken.DeviceToken;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SignUpRequest {

  @NotNull
  private Account account;

  @NotNull
  private DeviceToken deviceToken;
}
