package com.znaczek.agw.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

@Configuration
@EnableRedisWebSession
@ConditionalOnProperty(
  value="msagw.redis.enabled",
  havingValue = "true",
  matchIfMissing = false)
@Slf4j
public class RedisConfig {

  @Value("${msagw.redis.host}")
  private String host;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    log.info("REDIS HOST: {}", host);
    return new LettuceConnectionFactory(host, 6379);
  }
}
