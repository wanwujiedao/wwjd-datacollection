package com.wwjd.dc.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * data collection service interface
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018年12月03日 19:29:00
 */
public interface IDataCollectionService {

    /**
     *  procession kafka message
     *
     * @author adao
     * @time 2018/12/3 20:35
     * @CopyRight 万物皆导
     * @param record
     * @return
     */
    boolean dealCollection(ConsumerRecord<String, String> record);
}
