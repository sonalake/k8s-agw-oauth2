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

  @PostConstruct
  public void setup() {
    reactiveRedisSessionRepository.setDefaultMaxInactiveInterval(sessionTimeout);
  }
}
