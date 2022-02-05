package witch.endless.service.request.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import witch.endless.service.request.PatchRequest;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class AccountUpdateRequest implements PatchRequest {

  private AccountUpdate account;

  @AllArgsConstructor
  @Builder
  @Data
  @NoArgsConstructor
  public static class AccountUpdate {

    private String nickname;
  }
}
