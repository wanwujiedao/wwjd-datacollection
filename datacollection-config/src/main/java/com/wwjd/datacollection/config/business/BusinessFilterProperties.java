package com.wwjd.datacollection.config.business;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * The rules for Business intrusion
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2019年03月12日 10:49:00
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConfigurationProperties(prefix = "wwjd.business.filter")
public final class BusinessFilterProperties {
    /**
     * business filter
     */
    private Map<String,Rule> rules=new LinkedHashMap<>();

    public Map<String, Rule> getRules() {
        return rules;
    }

    public void setRules(Map<String, Rule> rules) {
        this.rules = rules;
    }

    /**
     *  rules
     *
     * @author adao
     * @CopyRight 万物皆导
     * @created 2019/3/12 10:55
     * @Modified_By adao 2019/3/12 10:55
     */
    public static class Rule{
        /**
         * topic for business
         */
       private String topic;
        /**
         * need datas
         */
        private Set<String> fields;
       /**
        * detail for key
        */
       private Map<String,String> keys = new LinkedHashMap<>();

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public Set<String> getFields() {
            return fields;
        }

        public void setFields(Set<String> fields) {
            this.fields = fields;
        }

        public Map<String, String> getKeys() {
            return keys;
        }

        public void setKeys(Map<String, String> keys) {
            this.keys = keys;
        }
    }

}
