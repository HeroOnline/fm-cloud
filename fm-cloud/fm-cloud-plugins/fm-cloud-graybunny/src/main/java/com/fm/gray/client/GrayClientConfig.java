package com.fm.gray.client;

public interface GrayClientConfig {


    boolean isGrayEnroll();

    int grayEnrollDealyTimeInMs();


    String getServerUrl();


    int getServiceUpdateIntervalTimerInMs();


    boolean isRetryable();

    int getRetryNumberOfRetries();

}
