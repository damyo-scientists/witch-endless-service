package witch.endless.service.factory.model.account;

import java.time.Instant;
import witch.endless.service.model.account.deviceToken.DeviceToken;
import witch.endless.service.model.account.deviceToken.DeviceType;

public class DeviceTokenFactory {

  private static final String MOCK_STR = "abcd";

  public static DeviceToken create() {
    return create(MOCK_STR + Instant.now().toEpochMilli());
  }

  public static DeviceToken create(String value) {
    return DeviceToken.builder()
        .deviceType(DeviceType.ANDROID)
        .value(value)
        .build();
  }
}
