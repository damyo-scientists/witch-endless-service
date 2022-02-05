package witch.endless.service.service.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import witch.endless.service.model.account.Account;
import witch.endless.service.model.account.deviceToken.DeviceToken;
import witch.endless.service.repository.account.AccountRepository;

@RequiredArgsConstructor
@Service
public class AccountService {

  private static final int RANDOM_NUMERIC_NUMBER_DIGIT = 8;
  private final AccountRepository accountRepository;

  public String generateUniqueKey() {
    String uniqueKey = null;
    Optional<Account> accountSearch = Optional.empty();
    while (uniqueKey == null || uniqueKey.startsWith("0") || accountSearch.isPresent()) {
      uniqueKey = RandomStringUtils.randomNumeric(RANDOM_NUMERIC_NUMBER_DIGIT);
      accountSearch = accountRepository.findByUniqueKey(uniqueKey);
    }
    return uniqueKey;
  }

  public List<DeviceToken> updateRecentDeviceToken(
      List<DeviceToken> recentTokens,
      DeviceToken deviceToken
  ) {
    if (recentTokens == null) {
      return new ArrayList<>(Collections.singletonList(deviceToken));
    }

    Optional<DeviceToken> search = recentTokens.stream()
        .filter(token -> token.getValue().equals(deviceToken.getValue()))
        .findFirst();
    if (search.isPresent()) {
      search.get().renew();
    } else {
      if (recentTokens.size() >= 3) {
        recentTokens.remove(recentTokens.get(recentTokens.size() - 1));
      }
      recentTokens.add(deviceToken);
    }
    Collections.sort(recentTokens);
    return recentTokens;
  }
}
