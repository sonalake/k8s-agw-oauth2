package com.znaczek.agw;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class DebugHelper {

//  restore to debug Hazelcast embedded
//  private final HazelcastInstance hazelcastInstance;
  private final ObjectMapper om;

  public void help(String prefix, ServerWebExchange exchange, WebSession s) {
    /* restore to debug Hazelcast embedded
    String state = exchange.getRequest().getQueryParams().getFirst(OAuth2ParameterNames.STATE);

    log.info(prefix + " request: {}", exchange.getRequest().getURI());
    log.info(prefix + " session id: {}", s.getId());
    try {
      log.info(prefix + " session map: {}", om.writeValueAsString(hazelcastInstance.getMap(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME)));
    } catch (Exception e) {
      log.error(prefix + " error parsing session map");
    }

    log.info(prefix + " state: {}", state);
    log.info(prefix + " session attributes: {}", s.getAttributes().keySet());
    Map<String, Object> m = (Map<String, Object>)s.getAttributes().get("org.springframework.security.oauth2.client.web.server.WebSessionOAuth2ServerAuthorizationRequestRepository.AUTHORIZATION_REQUEST");
    if (m == null) {
      log.info(prefix + " authorization_request map is null");
    } else {
      log.info(prefix + " authorization_request keys {}", m.keySet());
    }
     */
  }

}
