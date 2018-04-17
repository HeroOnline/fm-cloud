package com.fm.gray.client.config;

import com.fm.cloud.bamboo.BambooConstants;
import com.fm.cloud.bamboo.config.BambooConfiguration;
import com.fm.gray.GrayBunnyInitializingBean;
import com.fm.gray.client.*;
import com.fm.gray.client.config.properties.GrayBunnyProperties;
import com.fm.gray.core.GrayManager;
import com.fm.gray.core.InformationClient;
import com.fm.gray.core.RetryableInformationClient;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({GrayBunnyProperties.class})
@RibbonClients(defaultConfiguration = GrayRibbonClientsConfiguration.class)
public class GrayBunnyConfiguration {


    @Bean
    public BambooConfiguration.UnUseBambooIRule unUseBambooIRule() {
        return new BambooConfiguration.UnUseBambooIRule();
    }


//    @Bean
//    public IRule ribbonRule(@Autowired(required = false) IClientConfig config) {
//        GrayLoadBalanceRule rule = new GrayLoadBalanceRule();
//        rule.initWithNiwsConfig(config);
//        return rule;
//    }

    @Bean
    @Order(value = BambooConstants.INITIALIZING_ORDER + 1)
    public GrayBunnyInitializingBean grayBunnyInitializingBean() {
        return new GrayBunnyInitializingBean();
    }


    @Bean
    public InstanceLocalInfo instanceLocalInfo(@Autowired EurekaClient eurekaClient) {
        EurekaInstanceConfig instanceConfig = eurekaClient.getApplicationInfoManager().getEurekaInstanceConfig();
        InstanceLocalInfo localInfo = new InstanceLocalInfo();
        localInfo.setInstanceId(instanceConfig.getInstanceId());
        localInfo.setServiceId(instanceConfig.getAppname());
        localInfo.setGray(false);
        return localInfo;
    }


    @Bean
    @ConditionalOnMissingBean
    public GrayDecisionFactory grayDecisionFactory() {
        return new DefaultGrayDecisionFactory();
    }


    @Configuration
    @ConditionalOnProperty(prefix = "graybunny", value = "information-client", havingValue = "http", matchIfMissing = true)
    public static class HttpGrayManagerClientConfiguration {
        @Autowired
        private GrayBunnyProperties grayBunnyProperties;

        @Bean
        public InformationClient informationClient() {
            InformationClient client = new HttpInformationClient(grayBunnyProperties.getServerUrl(), new RestTemplate());
            if (!grayBunnyProperties.isRetryable()) {
                return client;
            }
            return new RetryableInformationClient(grayBunnyProperties.getRetryNumberOfRetries(), client);
        }


        @Bean
        public GrayManager grayManager(InformationClient informationClient, GrayDecisionFactory grayDecisionFactory) {
            GrayOptionalArgs args = new GrayOptionalArgs();
            args.setDecisionFactory(grayDecisionFactory);
            args.setGrayClientConfig(grayBunnyProperties);
            args.setInformationClient(informationClient);
            return new DefaultGrayManager(args);
        }
    }


}
