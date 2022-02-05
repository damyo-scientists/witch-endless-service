package witch.endless.service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import witch.endless.service.service.firebase.ClassPathFirebaseService;
import witch.endless.service.service.firebase.FirebaseService;

@Configuration
@Profile("!test")
@RequiredArgsConstructor
public class FirebaseConfiguration {

  private final AppConfig appConfig;

  @Bean
  public FirebaseService firebaseService() {
    return new ClassPathFirebaseService(appConfig.getFcm(), appConfig.getPlayStore());
  }
}
