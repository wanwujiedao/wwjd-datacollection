package com.wwjd.dc.controller;

import com.alibaba.fastjson.JSON;
import com.wwjd.config.init.DataCollectionInitProperties;
import com.wwjd.dc.constants.DataCollectionConstants;
import com.wwjd.dc.util.DealDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * DATA COLLECTION CONTOLLER
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018-11-29 13:26:00
 */
@RestController
public class DataCollectionController {

    /**
     * kafkaTemplate Autowired
     */
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private DataCollectionInitProperties dataCollectionInitProperties;

    @PostMapping("/pulsar")
    public final void collectionData(HttpServletRequest request) {
        // Security check
        Long userId = DealDataUtil.getUserIdFromRequest(request);

        // Processing Request Data
        Map<String, String> data = DealDataUtil.dealData(request, dataCollectionInitProperties.getHeaders());

        //　If the map is empty, this request will be kill
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        // user id
        data.put(DataCollectionConstants.USER_ID,userId == null? DataCollectionConstants.ZERO.toString():userId.toString());
        // Peak shaving and valley filling by kafka
        kafkaTemplate.send(DataCollectionConstants.DATA_COLLECTION_TOPIC, DealDataUtil.getRowKey(), JSON.toJSONString(data));
    }


}
