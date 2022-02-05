package witch.endless.service.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import witch.endless.service.security.entryPoint.JwtAuthenticationEntryPoint;
import witch.endless.service.security.filter.JwtRequestFilter;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  protected final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  protected final JwtRequestFilter jwtRequestFilter;

  @Autowired
  public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
      JwtRequestFilter jwtRequestFilter) {
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic().disable()  // security에서 기본으로 생성하는 login페이지 사용 안 함
        .csrf().disable()  // csrf 사용 안 함 == REST API 사용하기 때문에
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // JWT 인증 사용하므로 세션 사용 함
        .and()
        .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
        .antMatchers("/*/sign-in", "/*/sign-up", "/rewards").permitAll() // 가입 및 인증 주소는 누구나 접근가능
        .anyRequest().authenticated() // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .and()
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/v2/api-docs",
        "/configuration/ui",
        "/swagger-resources/**",
        "/configuration/security",
        "/api-docs/**",
        "/swagger-ui/**",
        "/webjars/**");
  }
}
