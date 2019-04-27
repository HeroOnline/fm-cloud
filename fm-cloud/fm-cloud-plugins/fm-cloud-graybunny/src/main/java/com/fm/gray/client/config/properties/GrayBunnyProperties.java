package com.fm.gray.client.config.properties;

import com.fm.gray.client.GrayClientConfig;
import com.fm.gray.core.RetryableInformationClient;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("graybunny")
public class GrayBunnyProperties implements GrayClientConfig{


    private int serviceUpdateIntervalTimerInMs = 60000;

    private String informationClient = "http";

    private String serverUrl = "http://localhost:10202";

    private boolean retryable = true;
    private int retryNumberOfRetries = RetryableInformationClient.DEFAULT_NUMBER_OF_RETRIES;

    private InstanceConfig instance = new InstanceConfig();


    public String getInformationClient() {
        return informationClient;
    }

    public void setInformationClient(String informationClient) {
        this.informationClient = informationClient;
    }

    @Override
    public boolean isGrayEnroll() {
        return instance.isGrayEnroll();
    }

    @Override
    public int grayEnrollDealyTimeInMs() {
        return instance.getGrayEnrollDealyTimeInMs();
    }

    public String getServerUrl() {
        return serverUrl;
    }

    @Override
    public int getServiceUpdateIntervalTimerInMs() {
        return serviceUpdateIntervalTimerInMs;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }


    public void setServiceUpdateIntervalTimerInMs(int serviceUpdateIntervalTimerInMs) {
        this.serviceUpdateIntervalTimerInMs = serviceUpdateIntervalTimerInMs;
    }

    public InstanceConfig getInstance() {
        return instance;
    }

    public void setInstance(InstanceConfig instance) {
        this.instance = instance;
    }


    public boolean isRetryable() {
        return retryable;
    }

    public void setRetryable(boolean retryable) {
        this.retryable = retryable;
    }

    public int getRetryNumberOfRetries() {
        return retryNumberOfRetries;
    }

    public void setRetryNumberOfRetries(int retryNumberOfRetries) {
        this.retryNumberOfRetries = retryNumberOfRetries;
    }

    /**
     * 实例
     */
    public class InstanceConfig{

        private boolean grayEnroll;
        private int grayEnrollDealyTimeInMs = 40000;

        public boolean isGrayEnroll() {
            return grayEnroll;
        }

        public void setGrayEnroll(boolean grayEnroll) {
            this.grayEnroll = grayEnroll;
        }

        public int getGrayEnrollDealyTimeInMs() {
            return grayEnrollDealyTimeInMs;
        }

        public void setGrayEnrollDealyTimeInMs(int grayEnrollDealyTimeInMs) {
            this.grayEnrollDealyTimeInMs = grayEnrollDealyTimeInMs;
        }
    }
}
