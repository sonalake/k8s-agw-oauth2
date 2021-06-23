package com.znaczek.agw.filters;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionFilter implements GlobalFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    return chain.filter(exchange)
      .then(exchange.getSession())
      .zipWith(
        exchange.getPrincipal()
          .cast(Authentication.class)
          .defaultIfEmpty(getEmptyAuthentication())
      )
      .doOnNext(z -> {
        WebSession s = z.getT1();
        Authentication a = z.getT2();
        Instant sessionExpiresIn =  s.getLastAccessTime().plus(s.getMaxIdleTime());
        var cb = ResponseCookie
          .from("SESSION_EXPIRES_IN", String.valueOf(sessionExpiresIn.getEpochSecond()))
          .path("/")
          .sameSite("Lax");
        if (!a.isAuthenticated()) {
          cb.maxAge(Duration.ZERO);
        }
        exchange.getResponse().addCookie(cb.build());
      }).then();
  }

  private Authentication getEmptyAuthentication() {
    return new Authentication() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
      }

      @Override
      public Object getCredentials() {
        return null;
      }

      @Override
      public Object getDetails() {
        return null;
      }

      @Override
      public Object getPrincipal() {
        return null;
      }

      @Override
      public boolean isAuthenticated() {
        return false;
      }

      @Override
      public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
      }

      @Override
      public String getName() {
        return null;
      }
    };
  }

}
