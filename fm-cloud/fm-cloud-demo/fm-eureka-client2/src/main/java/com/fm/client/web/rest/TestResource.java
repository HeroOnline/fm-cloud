package com.fm.client.web.rest;

import com.fm.client.event.BusEvent;
import com.fm.client.feign.TestClient;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by saleson on 2017/10/18.
 */
@RestController
@RequestMapping("/api/test")
public class TestResource implements ApplicationContextAware {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TestClient testClient;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> testGet(HttpServletRequest request) {
        String url = "http://eureka-client/api/test/get";
        String query = request.getQueryString();
        if (!StringUtils.isEmpty(query)) {
            url = url + "?" + query;
        }
//        if (!StringUtils.isEmpty(version)) {
//            url += "?version=" + version;
//        }
        Map map = restTemplate.getForObject(url, Map.class);
        if (map != null) {
            return map;
        } else {
            return ImmutableMap.of("test2", "success.");
        }
    }

    @RequestMapping(value = "/get2", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> testGet2(@RequestParam(value = "version", required = false) String version) {
        cxt.publishEvent(new BusEvent("test", cxt.getId(), "**"));
        Map map = testClient.testGet(version);
        if (map != null) {
            return map;
        } else {
            return ImmutableMap.of("test2", "success.");
        }
    }


    @RequestMapping(value = "/post", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> testPost(@RequestParam(value = "version", required = false) String version) {
        Map map = testClient.testPost(version);
        if (map != null) {
            return map;
        } else {
            return ImmutableMap.of("test2", "success.");
        }
    }


    @RequestMapping(value = "/post2", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> testPost2(@RequestParam(value = "version", required = false) String version) {
        Map map = testClient.testPost2(version);
        if (map != null) {
            return map;
        } else {
            return ImmutableMap.of("test2", "success.");
        }
    }

    private ApplicationContext cxt;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.cxt = applicationContext;
    }
}
