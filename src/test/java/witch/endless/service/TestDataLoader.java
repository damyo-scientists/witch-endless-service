package witch.endless.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import witch.endless.service.behavior.account.AccountBehavior;
import witch.endless.service.factory.model.account.AccountFactory;
import witch.endless.service.factory.model.account.DeviceTokenFactory;

@Component
@Profile("test")
public class TestDataLoader implements ApplicationRunner {

  @Autowired private AccountBehavior accountBehavior;

  public void run(ApplicationArguments args) {
    // auth
    accountBehavior.create(AccountFactory.create("alex@gmail.com"), DeviceTokenFactory.create());
  }
}
