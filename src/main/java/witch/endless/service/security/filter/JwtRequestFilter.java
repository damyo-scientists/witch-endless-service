package witch.endless.service.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import witch.endless.service.security.provider.JwtTokenProvider;
import witch.endless.service.service.auth.JwtAccountDetailsService;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtAccountDetailsService jwtAccountDetailService;

  @Autowired
  public JwtRequestFilter(JwtTokenProvider jwtTokenProvider,
      @Lazy JwtAccountDetailsService jwtAccountDetailsService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.jwtAccountDetailService = jwtAccountDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    final String requestTokenHeader = request.getHeader("Authorization");

    if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
      log.error("JWT Token does not begin with Bearer String");
      filterChain.doFilter(request, response);
      return;
    }

    String jwtToken = requestTokenHeader.substring(7);
    String uniqueKey = extractUniqueKey(jwtToken);

    if (uniqueKey != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = jwtAccountDetailService.loadUserByUsername(uniqueKey);

      if (jwtTokenProvider.validateToken(jwtToken, userDetails)) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }
    filterChain.doFilter(request, response);
  }

  private String extractUniqueKey(String jwtToken) {
    try {
      return jwtTokenProvider.getUniqueKeyFromToken(jwtToken);
    } catch (IllegalArgumentException e) {
      log.error("Unable to get JWT Token");
    } catch (ExpiredJwtException e) {
      log.error("JWT Token has expired");
    }
    return null;
  }
}
