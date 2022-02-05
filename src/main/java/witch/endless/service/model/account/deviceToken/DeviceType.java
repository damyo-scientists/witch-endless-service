package witch.endless.service.model.account.deviceToken;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Device token type")
public enum DeviceType {
  ANDROID, IOS
}
