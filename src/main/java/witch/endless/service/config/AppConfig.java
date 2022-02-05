package witch.endless.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Data
@EnableConfigurationProperties
public class AppConfig {

  @NestedConfigurationProperty
  private FcmConfig fcm;

  @NestedConfigurationProperty
  private PlayStoreConfig playStore;

  @Configuration
  @ConfigurationProperties("fcm")
  @Data
  public static class FcmConfig {

    private String serviceAccountJson;
    private String databaseUrl;
  }

  @Configuration
  @ConfigurationProperties("play-store")
  @Data
  public static class PlayStoreConfig {

    private String serviceAccountJson;
  }
}
