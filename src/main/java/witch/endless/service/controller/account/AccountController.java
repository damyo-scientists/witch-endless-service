package witch.endless.service.controller.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import witch.endless.service.behavior.account.AccountBehavior;
import witch.endless.service.model.account.Account;
import witch.endless.service.request.account.AccountUpdateRequest;
import witch.endless.service.response.account.AccountResponse;
import witch.endless.service.response.account.AccountsResponse;

@RequestMapping("/accounts")
@RequiredArgsConstructor
@RestController
@Tag(name = "Account")
public class AccountController {

  private final AccountBehavior behavior;

  @GetMapping
  @Operation(summary = "Get all account", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<AccountsResponse> getAll() {
    return ResponseEntity.ok(AccountsResponse.builder().accounts(behavior.getAll()).build());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get account by id", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<AccountResponse> getById(@PathVariable("id") final long id) {
    Optional<Account> oAccount = behavior.findById(id);
    return oAccount
        .map(account -> ResponseEntity.ok(new AccountResponse(account)))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/@{email}")
  @Operation(summary = "Get user by email", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<AccountResponse> getByEmail(@PathVariable("email") final String email) {
    Optional<Account> accountOptional = behavior.getByEmail(email);
    return accountOptional
        .map(account -> ResponseEntity.ok(AccountResponse.builder().account(account).build()))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(summary = "Patch account", security = @SecurityRequirement(name = "bearerAuth"))
  @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json")
  public ResponseEntity<AccountResponse> updateById(
      @PathVariable("id") final long id,
      @RequestBody AccountUpdateRequest updateRequst
  ) {
    Account newbie = behavior.update(behavior.fetchById(id), updateRequst.getAccount());
    return ResponseEntity.ok(AccountResponse.builder().account(newbie).build());
  }

  @DeleteMapping(path = "/{id}")
  @Operation(summary = "Delete account", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<Void> deleteById(
      @PathVariable("id") final long id
  ) {
    behavior.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
