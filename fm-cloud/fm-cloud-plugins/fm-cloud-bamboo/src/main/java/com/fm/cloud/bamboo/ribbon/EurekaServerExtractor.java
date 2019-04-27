package com.fm.cloud.bamboo.ribbon;

import com.netflix.loadbalancer.Server;
import org.springframework.cloud.netflix.ribbon.DefaultServerIntrospector;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.util.Map;

/**
 * 获取服务实例的相关信息
 */
public class EurekaServerExtractor {


    private SpringClientFactory clientFactory;

    public EurekaServerExtractor(SpringClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    public ServerIntrospector serverIntrospector(String serviceId) {
        ServerIntrospector serverIntrospector = this.clientFactory.getInstance(serviceId,
                ServerIntrospector.class);
        if (serverIntrospector == null) {
            serverIntrospector = new DefaultServerIntrospector();
        }
        return serverIntrospector;
    }

    /**
     * 获取实例的metadata信息
     *
     * @param serviceId
     * @param server
     * @return
     */
    public Map<String, String> getServerMetadata(String serviceId, Server server) {
        return serverIntrospector(serviceId).getMetadata(server);
    }

}
