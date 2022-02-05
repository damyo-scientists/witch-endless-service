package witch.endless.service.factory.model.account;


import witch.endless.service.model.account.Account;
import witch.endless.service.model.account.Role;

public class AccountFactory {

  private static final String MOCK_UID = "vTtfz7zpU1bIR9kIDc8gOpUK8q92";

  public static Account create(String email) {
    return Account.builder()
        .email(email)
        .firebaseUid(MOCK_UID)
        .role(Role.USER)
        .build();
  }
}
