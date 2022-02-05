package witch.endless.service.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * JWT토큰 생성 및 유효성을 검증하는 컴포넌트
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String secretKey;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String getUniqueKeyFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }

  private boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  public boolean validateToken(String jwtToken, UserDetails userDetails) {
    final String uniqueKey = getUniqueKeyFromToken(jwtToken);
    return (uniqueKey.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);

//    try {
//      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
//      return true;
//    } catch (SignatureException e) {
//      log.error(String.format("Invalid JWT signature -> Message: %s", e));
//    } catch (MalformedJwtException e) {
//      log.error(String.format("Invalid JWT token -> Message: %s", e));
//    } catch (ExpiredJwtException e) {
//      log.error(String.format("Expired JWT token -> Message: %s", e));
//    } catch (UnsupportedJwtException e) {
//      log.error(String.format("Unsupported JWT token -> Message: %s", e));
//    } catch (IllegalArgumentException e) {
//      log.error(String.format("JWT claims string is empty -> Message: %s", e));
//    }
  }
}

