package com.fm.cloud.bamboo.zuul.filter;

import com.fm.cloud.bamboo.BambooRequest;
import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import java.util.stream.Collectors;

/**
 * 主要作用是用来获取request的相关信息，为后面的路由提供数据基础。
 */
public class BambooPreZuulFilter extends ZuulFilter{
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 10000;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        BambooRequest.Builder builder = BambooRequest.builder()
                .serviceId((String)context.get(FilterConstants.SERVICE_ID_KEY))
                .uri((String)context.get(FilterConstants.REQUEST_URI_KEY))
                .addMultiParams(context.getRequestQueryParams())
                .addHeaders(context.getZuulRequestHeaders())
                .addHeaders(context.getOriginResponseHeaders().stream().collect(Collectors.toMap(Pair::first, Pair::second)));
        context.getOriginResponseHeaders().forEach(pair-> builder.addHeader(pair.first(), pair.second()));

        BambooRequest bambooRequest = builder.build();

        com.fm.cloud.bamboo.BambooRequestContext.initRequestContext(bambooRequest, bambooRequest.getParams().getFirst("version"));
        return null;
    }
}
