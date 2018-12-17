package com.wwjd.config.init;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data collection initualize configuration
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018-12-17 11:29:00
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConfigurationProperties(prefix = "wwjd.dc.cofig")
public final class DataCollectionInitProperties {
    /**
     * headers
     */
    private Set<String> headers;
    /**
     * family mapping to column
     */
    private Map<String, List<String>> familyMappingColumn = new LinkedHashMap<>();
    /**
     * appkey
     */
    private Map<String, Integer> appkey = new LinkedHashMap<>();

    /**
     * paramters mapping
     */
    private Map<String,String> parameterMapping = new LinkedHashMap<>();
    /**
     * eventList mapping
     */
    private Map<String,String> eventListMapping = new LinkedHashMap<>();

    public Set<String> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<String> headers) {
        this.headers = headers;
    }

    public Map<String, List<String>> getFamilyMappingColumn() {
        return familyMappingColumn;
    }

    public void setFamilyMappingColumn(Map<String, List<String>> familyMappingColumn) {
        this.familyMappingColumn = familyMappingColumn;
    }

    public Map<String, Integer> getAppkey() {
        return appkey;
    }

    public void setAppkey(Map<String, Integer> appkey) {
        this.appkey = appkey;
    }

    public Map<String, String> getParameterMapping() {
        return parameterMapping;
    }

    public void setParameterMapping(Map<String, String> parameterMapping) {
        this.parameterMapping = parameterMapping;
    }

    public Map<String, String> getEventListMapping() {
        return eventListMapping;
    }

    public void setEventListMapping(Map<String, String> eventListMapping) {
        this.eventListMapping = eventListMapping;
    }
}
