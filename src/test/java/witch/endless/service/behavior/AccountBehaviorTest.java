package witch.endless.service.behavior;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import witch.endless.service.behavior.account.AccountBehavior;
import witch.endless.service.factory.model.account.DeviceTokenFactory;
import witch.endless.service.model.account.Account;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountBehaviorTest {

  @Autowired
  private AccountBehavior behavior;

  @Test
  public void testGetByEmail() {
    Account account = behavior.getByEmail("alex@gmail.com").orElse(null);
    assertThat(account).isNotNull();
    assertThat(account.getId()).isEqualTo(1L);
    assertThat(account.getJwtSecret()).isNotNull();
    assertThat(account.getUniqueKey()).isNotNull();
    assertThat(account.getNickname()).isEqualTo(account.getUniqueKey());
    assertThat(account.getCreatedAt()).isNotNull();
    assertThat(account.getUpdatedAt()).isNotNull();

    assertThat(behavior.getByEmail("alexich@gmail.com")).isEmpty();
  }

  @Test
  public void testGetById() {
    Account account = behavior.findById(1L).orElse(null);
    assertThat(account).isNotNull();
    assertThat(account.getId()).isEqualTo(1L);
    assertThat(account.getJwtSecret()).isNotNull();
    assertThat(account.getUniqueKey()).isNotNull();
    assertThat(account.getNickname()).isEqualTo(account.getUniqueKey());
    assertThat(account.getCreatedAt()).isNotNull();
    assertThat(account.getUpdatedAt()).isNotNull();

    assertThat(behavior.findById(99999L)).isEmpty();
  }

  @Test
  public void testCreate() {
    Account account = Account.builder()
        .email("alex2@gmail.com")
        .build();
    account = behavior.create(account, DeviceTokenFactory.create()).orElseThrow();

    assertThat(account).isNotNull();
    assertThat(account.getUniqueKey()).isNotNull();
    assertThat(account.getNickname()).isEqualTo(account.getUniqueKey());

    account = Account.builder()
        .email("alex3@gmail.com")
        .build();
    assertThat(account).isNotNull();

    account = Account.builder()
        .build();

    Account finalAccount = account;
    assertThatThrownBy(() -> behavior.create(finalAccount, DeviceTokenFactory.create()))
        .isInstanceOf(ValidationException.class);
  }
}
