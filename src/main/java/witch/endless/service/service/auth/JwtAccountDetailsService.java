package witch.endless.service.service.auth;


import com.google.firebase.auth.FirebaseAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import witch.endless.service.behavior.account.AccountBehavior;
import witch.endless.service.model.account.Account;
import witch.endless.service.repository.account.AccountRepository;
import witch.endless.service.request.auth.SignInRequest;
import witch.endless.service.service.firebase.FirebaseService;

@RequiredArgsConstructor
@Service
public class JwtAccountDetailsService implements UserDetailsService {

  @Value("${jwt.secret}")
  private String secretKey;
  @Value("${jwt.expiration}")
  private long expiration;

  private final AccountBehavior accountBehavior;
  private final AccountRepository accountRepository;
  private final FirebaseService firebaseService;

  @PostConstruct
  protected void init() {
    this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  /**
   * Wds's username = uniqueKey
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account account = accountBehavior.findByUniqueKey(username)
        .orElseThrow(() -> new UsernameNotFoundException("UniqueKey not found"));

    return User.builder()
        .username(account.getUniqueKey())
        .password(account.getFirebaseUid())
        .roles(account.getRole().toString())
        .build();
  }

  public Optional<Account> authenticate(SignInRequest signInRequest) throws FirebaseAuthException {
    var decodedToken = firebaseService.authenticate(signInRequest.getFirebaseJwt());
    Optional<Account> optionalAccount = accountRepository.findByEmail(decodedToken.getEmail());
    if (optionalAccount.isPresent()) {
      Account account = optionalAccount.get();
      if (account.getFirebaseUid().equals(decodedToken.getUid())) {
        return Optional.of(account);
      }
      return Optional.empty();
    }
    return Optional.empty();
  }


  public String createJwtToken(Account account) {
    Claims claims = Jwts.claims().setSubject(account.getUniqueKey());
    claims.put("token", account.getJwtSecret());
    Date now = new Date(System.currentTimeMillis());
    return Jwts.builder()
        .setClaims(claims) // 데이터
        .setIssuedAt(now) // 토큰 발행일자
        .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Set Expire Time
        .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
        .compact();
  }
}
