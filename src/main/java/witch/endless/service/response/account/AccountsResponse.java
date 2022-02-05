package witch.endless.service.response.account;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import witch.endless.service.model.account.Account;

@Builder
@Data
public class AccountsResponse {

  private List<Account> accounts;
}
