package com.znaczek.agw.imdg;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class InstanceConfig {

  @Value("msagw:hazelcast-dns")
  private String hazelcastDns;

  @Bean
  @Profile("default")
  public Config defaultConfig() {
    Config config = new Config();
    NetworkConfig networkConfig = config.getNetworkConfig();
    networkConfig.getJoin().getMulticastConfig().setEnabled(false);
    networkConfig.getJoin().getTcpIpConfig().setEnabled(false);
    networkConfig.getJoin().getKubernetesConfig().setEnabled(true);
    networkConfig.getJoin().getKubernetesConfig()
      .setProperty("service-dns", hazelcastDns);
    return config;
  }

  @Bean
  @Profile("dev")
  public Config devConfig() {
    Config config = new Config();
    NetworkConfig networkConfig = config.getNetworkConfig();
    networkConfig.getJoin().getMulticastConfig().setEnabled(false);
    networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
    networkConfig.getJoin().getTcpIpConfig().addMember("127.0.0.1");
    return config;
  }
}
