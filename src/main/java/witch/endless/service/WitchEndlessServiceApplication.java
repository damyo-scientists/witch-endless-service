package witch.endless.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WitchEndlessServiceApplication {

  public static final String DATA_SNAPSHOT_VERSION = "2021-05-01";

  public static void main(String[] args) {
    SpringApplication.run(WitchEndlessServiceApplication.class, args);
  }
}
