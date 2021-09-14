package com.znaczek.agw.session;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.ReactiveRedisSessionRepository;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class RedisWebSessionOverride {

  @Value("${msagw.session-timeout}")
  private int sessionTimeout;

  private final ReactiveRedisSessionRepository reactiveRedisSessionRepository;

  /**
   * Session timeout can be configured within @EnableRedisWebSession annotation byt only with a static value.
   * To allow configuring it from properties we need to invoke setter method `setDefaultMaxInactiveInterval` on ReactiveRedisSessionRepository.
   */
  @PostConstruct
  public void setup() {
    reactiveRedisSessionRepository.setDefaultMaxInactiveInterval(sessionTimeout);
  }
}
