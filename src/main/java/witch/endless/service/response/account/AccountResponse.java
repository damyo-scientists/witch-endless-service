package witch.endless.service.response.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import witch.endless.service.model.account.Account;

@AllArgsConstructor
@Builder
@Data
public class AccountResponse {

  private Account account;
}
