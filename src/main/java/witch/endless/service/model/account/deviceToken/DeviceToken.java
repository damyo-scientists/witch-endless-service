package witch.endless.service.model.account.deviceToken;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import witch.endless.service.model.BaseEntity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "Device info")
@SuperBuilder
public class DeviceToken extends BaseEntity implements Comparable<DeviceToken> {

  @Enumerated
  @NotNull
  private DeviceType deviceType;

  @Column
  @NotEmpty
  @Schema(description = "Device token value")
  private String value;

  public void renew() {
    this.onUpdate();
  }

  @Override
  public int compareTo(DeviceToken o) {
    if (o == null || this.updatedAt == null) {
      return -1;
    }
    if (this.updatedAt.isAfter(o.updatedAt)) {
      return -1;
    } else {
      return 1;
    }
  }
}
