package com.wwjd.datacollection.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

/**
 * data collection service interface
 *
 * @author adao
 * @CopyRight qtshe
 * @Created 2018年12月03日 19:29:00
 */
public interface IDataCollectionService {

    /**
     *  procession kafka message
     *
     * @author adao
     * @time 2018/12/3 20:35
     * @CopyRight 杭州弧途科技有限公司（qtshe）
     * @param record
     * @return
     */
    boolean dealCollection(ConsumerRecord<String, String> record);


    /**
     *  procession data by day
     *
     * @author adao
     * @time 2018/12/3 20:35
     * @CopyRight 杭州弧途科技有限公司（qtshe）
     * @param map
     * @param rowKey
     * @return
     */
    boolean dealCollectionDay(Map<String, String> map, String rowKey);
}
