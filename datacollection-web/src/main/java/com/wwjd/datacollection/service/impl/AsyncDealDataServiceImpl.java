package com.wwjd.datacollection.service.impl;

import com.alibaba.fastjson.JSON;
import com.wwjd.datacollection.config.business.BusinessFilterProperties;
import com.wwjd.datacollection.config.hbase.HbaseBean;
import com.wwjd.datacollection.constants.DataCollectionConstants;
import com.wwjd.datacollection.service.IAsyncDealDataService;
import com.wwjd.datacollection.service.business.ICriteria;
import com.wwjd.datacollection.service.business.core.CoreCriteria;
import com.wwjd.datacollection.service.business.core.CriteriaChain;
import com.wwjd.datacollection.util.DealDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * async do something service implement
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018年12月14日 16:12:00
 */
@Service
public  class AsyncDealDataServiceImpl implements IAsyncDealDataService {


    /**
     * business filter
     */
    @Autowired
    private BusinessFilterProperties businessFilterProperties;

    /**
     * kafkaTemplate Autowired
     */
    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * async do something
     *
     * @param map
     * @return
     * @author adao
     * @time 2018/12/14 16:11
     * @CopyRight 万物皆导
     */
    @Async
    @Override
    public void dealThirdSomething(Map<String, List<HbaseBean>> map) {
        // get rules
        Map<String, BusinessFilterProperties.Rule> rules = businessFilterProperties.getRules();
        // if rules are empty,end the execution of the current thread.
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }
        // if map is empty,end the execution of the current thread.
        if (CollectionUtils.isEmpty(map)) {
            return;
        }

        // deal some filter then send message
        AsyncDealDataServiceHelper.sendKafkaMsg(kafkaTemplate, AsyncDealDataServiceHelper.getFilterData(rules, AsyncDealDataServiceHelper.tranMap(map)));
    }


    /**
     *  异步处理辅助类
     *
     * @author adao
     * @CopyRight 万物皆导
     * @created 2019/3/12 15:36
     * @Modified_By adao 2019/3/12 15:36
     */

    final static class AsyncDealDataServiceHelper {
        /**
         *
         *
         * @author adao
         * @time 2019/3/12 15:34
         * @CopyRight 万物皆导
         * @param kafkaTemplate
         * @param mapMsg
         * @return
         */
        private static void sendKafkaMsg(KafkaTemplate kafkaTemplate, Map<String, List<Map<String, String>>> mapMsg) {
            if (CollectionUtils.isEmpty(mapMsg)) {
                return;
            }
            // send message
            mapMsg.forEach((topic, list) -> kafkaTemplate.send(topic, JSON.toJSONString(list)));
        }


        /**
         * convert map to list
         *
         * @param map
         * @return
         * @author adao
         * @time 2019/3/12 15:22
         * @CopyRight 万物皆导
         */
        private static List<Map<String, String>> tranMap(Map<String, List<HbaseBean>> map) {

            // declare result
            List<Map<String, String>> rs = new ArrayList<>();

            // ergodic columns
            map.forEach((key, hbaseBeans) -> {
                // init map
                Map<String, String> mapColumn = new HashMap<>(DataCollectionConstants.SIXTEEN);
                if (!CollectionUtils.isEmpty(hbaseBeans)) {
                    // ergodic hbasebean
                    hbaseBeans.forEach(hbaseBean -> mapColumn.put(DealDataUtil.camelName(hbaseBean.getQualifier()), hbaseBean.getValue()));
                    // add to result
                    rs.add(mapColumn);
                }
            });
            // return result
            return rs;
        }

        /**
         * processing filter
         *
         * @author adao
         * @time 2019/3/12 15:28
         * @CopyRight 万物皆导
         * @param rules
         * @param listColumns
         * @return
         */
        private static Map<String, List<Map<String, String>>> getFilterData(Map<String, BusinessFilterProperties.Rule> rules, List<Map<String, String>> listColumns) {
            // declare result
            Map<String, List<Map<String, String>>> rs = new HashMap<>(DataCollectionConstants.SIXTEEN);

            rules.forEach((business, rule) -> {
                // copy list
                List<Map<String, String>> listColumnsCopy = new ArrayList<>();
                listColumnsCopy.addAll(listColumns);
                // topic
                String topic = rule.getTopic();

                List<ICriteria> criteriaList = new ArrayList<>();
                // rule key
                rule.getKeys().forEach((name, rulePattern) -> criteriaList.add(new CoreCriteria(name, rulePattern)));
                // declare chain
                ICriteria criteriaChain = new CriteriaChain(criteriaList);
                // do chain
                criteriaChain.meetCriteria(listColumnsCopy);
                // if not empty then add in list
                if (!CollectionUtils.isEmpty(listColumnsCopy)) {
                    rs.put(topic, listColumnsCopy);
                }
            });
            // return result
            return rs;
        }
    }

}
