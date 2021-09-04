package com.znaczek.agw.imdg;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
  value="msagw.redis.enabled",
  havingValue = "false",
  matchIfMissing = true)
public class InstanceConfig {

  @Value("${msagw.hazelcast.dns}")
  private String hazelcastDns;

  @Bean
  @Profile("default")

  public Config defaultConfig() {
    Config config = commonConfig();
    config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
    config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true);
    config.getNetworkConfig().getJoin().getKubernetesConfig()
      .setProperty("service-dns", hazelcastDns);
    return config;
  }

  @Bean
  @Profile("!default")
  public Config devConfig() {
    Config config = commonConfig();
    config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
    config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1");
    return config;
  }

  private Config commonConfig() {
    Config config = new Config();
    NetworkConfig networkConfig = config.getNetworkConfig();
    networkConfig.getJoin().getMulticastConfig().setEnabled(false);
    return config;
  }


}
