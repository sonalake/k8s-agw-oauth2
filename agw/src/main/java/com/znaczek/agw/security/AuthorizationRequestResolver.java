package com.znaczek.agw.security;

import com.znaczek.agw.DebugHelper;
import com.znaczek.agw.i18n.LangResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthorizationRequestResolver implements ServerOAuth2AuthorizationRequestResolver {

  private final LangResolver langResolver;

  private ServerOAuth2AuthorizationRequestResolver defaultResolver;

  private final DebugHelper debugHelper;

  public AuthorizationRequestResolver(ReactiveClientRegistrationRepository repo, LangResolver langResolver, DebugHelper debugHelper) {
    this.langResolver = langResolver;
    this.defaultResolver = new DefaultServerOAuth2AuthorizationRequestResolver(repo);
    this.debugHelper = debugHelper;
  }

  @Override
  public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
    Mono<OAuth2AuthorizationRequest> req = defaultResolver.resolve(exchange);
    if (req != null) {
      req = customizeAuthorizationRequest(req, exchange);
    }
    return req;
  }

  @Override
  public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
    Mono<OAuth2AuthorizationRequest> req = defaultResolver.resolve(exchange, clientRegistrationId);
    if (req != null) {
      req = customizeAuthorizationRequest(req, exchange);
    }
    return req;
  }

  private Mono<OAuth2AuthorizationRequest> customizeAuthorizationRequest(Mono<OAuth2AuthorizationRequest> req, ServerWebExchange exchange) {

    return exchange
      .getSession()
      .doOnNext(s -> debugHelper.help("RESOLVER", exchange, s))
      .flatMap(a -> req)
      .map(r -> {
        log.info("RESOLVER state to be stored: {}", r.getState());
        Map<String, Object> extraParams = new HashMap<>(r.getAdditionalParameters());
        extraParams.put("ui_locales", langResolver.resolveISOLang(exchange.getRequest()));

        return OAuth2AuthorizationRequest
          .from(r)
          .additionalParameters(extraParams)
          .build();
      }
    );
  }

}
