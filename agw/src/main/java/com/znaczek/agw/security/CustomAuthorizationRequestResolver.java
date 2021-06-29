package com.znaczek.agw.security;

import com.znaczek.agw.i18n.LangResolver;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
public class CustomAuthorizationRequestResolver implements ServerOAuth2AuthorizationRequestResolver {

  private final LangResolver langResolver;

  private ServerOAuth2AuthorizationRequestResolver defaultResolver;

  public CustomAuthorizationRequestResolver(ReactiveClientRegistrationRepository repo, LangResolver langResolver) {
    this.langResolver = langResolver;
    this.defaultResolver = new DefaultServerOAuth2AuthorizationRequestResolver(repo);
  }

  @Override
  public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
    Mono<OAuth2AuthorizationRequest> req = defaultResolver.resolve(exchange);
    if (req != null) {
      req = customizeAuthorizationRequest(req, exchange.getRequest());
    }
    return req;
  }

  @Override
  public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
    Mono<OAuth2AuthorizationRequest> req = defaultResolver.resolve(exchange, clientRegistrationId);
    if (req != null) {
      req = customizeAuthorizationRequest(req, exchange.getRequest());
    }
    return req;
  }

  private Mono<OAuth2AuthorizationRequest> customizeAuthorizationRequest(Mono<OAuth2AuthorizationRequest> req, ServerHttpRequest request) {
    return req.map(r -> {
        Map<String, Object> extraParams = new HashMap<>(r.getAdditionalParameters());
        extraParams.put("ui_locales", langResolver.resolveISOLang(request));

        return OAuth2AuthorizationRequest
          .from(r)
          .additionalParameters(extraParams)
          .build();
      }
    );
  }





}
