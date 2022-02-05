package witch.endless.service.controller.account;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import witch.endless.service.behavior.account.AccountBehavior;
import witch.endless.service.model.account.Account;
import witch.endless.service.request.auth.SignInRequest;
import witch.endless.service.request.auth.SignUpRequest;
import witch.endless.service.response.account.AccountResponse;
import witch.endless.service.response.auth.JwtResponse;
import witch.endless.service.service.auth.JwtAccountDetailsService;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
@Tag(name = "Auth")
public class AuthController {

  private final AccountBehavior behavior;
  private final JwtAccountDetailsService jwtAccountDetailsService;

  @Operation(summary = "Sign up account")
  @PostMapping(value = "/sign-up")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<AccountResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
    var account = signUpRequest.getAccount();
    var deviceToken = signUpRequest.getDeviceToken();
    if (account.getEmail() == null
        || account.getFirebaseUid() == null
        || deviceToken.getValue() == null
        || deviceToken.getDeviceType() == null) {
      return ResponseEntity.unprocessableEntity().build();
    }

    Optional<Account> oAccountCreated = behavior.create(account, deviceToken);

    return oAccountCreated.map(accountCreated ->
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(AccountResponse.builder().account(accountCreated).build())
    ).orElseGet(() -> ResponseEntity.unprocessableEntity().build());

  }

  @Operation(summary = "Sign in account. JWT Token is issued as a result of sign in.")
  @PostMapping("/sign-in")
  public ResponseEntity<JwtResponse> signIn(@RequestBody @Valid SignInRequest signInRequest)
      throws FirebaseAuthException {
    Optional<Account> optionalAccount = jwtAccountDetailsService.authenticate(signInRequest);
    if (optionalAccount.isEmpty()) {
      return ResponseEntity.unprocessableEntity().build();
    }
    Account account = optionalAccount.get();
    final String token = jwtAccountDetailsService.createJwtToken(account);

    JwtResponse jwtResponse = JwtResponse.builder().token(token).build();
    return ResponseEntity.ok(jwtResponse);
  }
}
