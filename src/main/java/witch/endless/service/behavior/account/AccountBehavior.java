package witch.endless.service.behavior.account;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import witch.endless.service.behavior.BaseBehavior;
import witch.endless.service.model.account.Account;
import witch.endless.service.model.account.ProviderType;
import witch.endless.service.model.account.deviceToken.DeviceToken;
import witch.endless.service.repository.account.AccountRepository;
import witch.endless.service.request.account.AccountUpdateRequest.AccountUpdate;
import witch.endless.service.service.auth.AccountService;
import witch.endless.service.utils.patch.Patcher;

@Component
@RequiredArgsConstructor
public class AccountBehavior implements BaseBehavior<Account, AccountRepository> {

  private final Patcher patcher;
  private final AccountRepository repo;
  private final AccountService service;

  public Optional<Account> findByUniqueKey(String uniqueKey) {
    return repo.findByUniqueKey(uniqueKey);
  }

  public Optional<Account> getByEmail(String email) {
    return repo.findByEmail(email);
  }

  @Transactional
  public Optional<Account> create(Account account, DeviceToken deviceToken) {
    if (repo.findByEmail(account.getEmail()).isPresent()) {
      return Optional.empty();
    }

    setAccountValues(account);

    var tokens = service.updateRecentDeviceToken(account.getRecentDeviceTokens(), deviceToken);
    account.setRecentDeviceTokens(tokens);
    account = repo.save(account);
    return Optional.of(account);
  }

  private void setAccountValues(Account account) {
    account.setUniqueKey(service.generateUniqueKey());
    account.setNickname(account.getUniqueKey());
    // TODO: fix
    account.setProviderType(ProviderType.GOOGLE);
  }

  @Override
  public AccountRepository getRepo() {
    return repo;
  }

  public Account update(Account oldbie, AccountUpdate update) {
    Account newbie = patcher.mergePatch(oldbie, update, Account.class);
    return repo.save(newbie);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    repo.deleteById(id);
  }
}
