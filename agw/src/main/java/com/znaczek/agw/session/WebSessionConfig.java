package com.znaczek.agw.session;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.session.MapSession;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WebSessionConfig {

  @Value("${msagw.session-timeout}")
  private String sessionTimeout;

  private final HazelcastInstance hazelcastInstance;

  @Bean
  public ReactiveSessionRepository<MapSession> reactiveSessionRepository() {
    final IMap<String, Session> map = hazelcastInstance.getMap(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME);
    var sr = new ReactiveMapSessionRepository(map);
    sr.setDefaultMaxInactiveInterval(Integer.parseInt(sessionTimeout));
    return sr;
  }
}
