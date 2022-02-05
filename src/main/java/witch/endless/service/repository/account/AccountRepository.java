package witch.endless.service.repository.account;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import witch.endless.service.model.account.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByEmail(String email);

  Optional<Account> findByFirebaseUid(String firebaseUid);

  Optional<Account> findByUniqueKey(String uniqueKey);
}
