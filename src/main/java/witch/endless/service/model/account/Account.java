package witch.endless.service.model.account;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.minidev.json.annotate.JsonIgnore;
import witch.endless.service.model.BaseEntity;
import witch.endless.service.model.account.deviceToken.DeviceToken;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "Account info")
@SuperBuilder
public class Account extends BaseEntity {

  @Column(length = 300, nullable = false, unique = true)
  @NotEmpty
  @Schema(description = "Email value of account")
  private String email;

  @Enumerated
  @NotNull
  @Schema(description = "Provider type of account")
  private ProviderType providerType;

  @Column
  @NotEmpty
  private String nickname;

  @Column(nullable = false, unique = true)
  @NotEmpty
  @Schema(accessMode = READ_ONLY)
  private String uniqueKey;

  @Builder.Default
  @Enumerated
  @NotNull
  @Schema(accessMode = READ_ONLY)
  private Role role = Role.USER;

  @Hidden
  @NotNull
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("updatedAt DESC")
  private List<DeviceToken> recentDeviceTokens;

  @Column
  @JsonIgnore
  private String firebaseUid;

  @Column
  private BigDecimal gold;

  @Column
  private BigDecimal jewel;

  @Builder.Default
  @Column(nullable = false)
  @JsonIgnore
  @Schema(accessMode = READ_ONLY, hidden = true)
  private UUID jwtSecret = UUID.randomUUID();
}
