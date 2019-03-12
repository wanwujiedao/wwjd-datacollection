package com.wwjd.datacollection.kafka;

import com.wwjd.datacollection.constants.DataCollectionConstants;
import com.wwjd.datacollection.service.IDataCollectionService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * kafka consumer
 *
 * @author adao
 * @CopyRight 万物皆导
 * @created 2018/11/29 14:56
 * @Modified_By adao 2018/11/29 14:56
 */
@Component
public final class KafkaMsgListener {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * autowored dataCollectionService
     */
    @Autowired
    private IDataCollectionService dataCollectionService;

    /**
     * dataCollection Listener
     *
     * @param record
     * @param acknowledgment
     * @return
     * @author adao
     * @time 2018/12/3 19:26
     * @CopyRight 万物皆导
     */
    @KafkaListener(topics = {DataCollectionConstants.DATA_COLLECTION_TOPIC})
    public void dataCollectionListen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        // procession something in dataCollectionService
        if (dataCollectionService.dealCollection(record)) {
            // submit message offset
            acknowledgment.acknowledge();
        }
    }
}
