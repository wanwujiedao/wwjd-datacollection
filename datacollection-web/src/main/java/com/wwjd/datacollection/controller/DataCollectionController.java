package com.wwjd.datacollection.controller;

import com.alibaba.fastjson.JSON;
import com.qts.datacollection.config.init.DataCollectionInitProperties;
import com.wwjd.datacollection.constants.DataCollectionConstants;
import com.wwjd.datacollection.service.IDataCollectionService;
import com.wwjd.datacollection.util.DateUtils;
import com.wwjd.datacollection.util.DealDataUtil;
import com.wwjd.datacollection.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * DATA COLLECTION CONTOLLER
 *
 * @author adao
 * @CopyRight qtshe
 * @Created 2018-11-29 13:26:00
 */
@RestController
public final class DataCollectionController {

    /**
     * kafkaTemplate Autowired
     */
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private DataCollectionInitProperties dataCollectionInitProperties;
    /**
     * data collection service
     */
    @Autowired
    private IDataCollectionService dataCollectionService;

    @RequestMapping("/pulsar")
    @SuppressWarnings("uncheked")
    public final Result<Boolean> collectionData(HttpServletRequest request) {
        Result<Boolean> rs=new Result<>();
        try {
        // Security check
        Long userId = DealDataUtil.getUserIdFromRequest(request);

        // Processing Request Data
        Map<String, String> data = DealDataUtil.dealData(request, dataCollectionInitProperties.getHeaders());

        //　If the map is empty, this request will be kill
            if (CollectionUtils.isEmpty(data)) {
                rs.setCode(3000);
                rs.setSuccess(false);
                rs.setMsg("Data is Empty!");
                rs.setData(false);
            }else {
                // user id
                data.put(DealDataUtil.humpToUnderline(DataCollectionConstants.USER_ID), userId == null ? DataCollectionConstants.ZERO.toString() : userId.toString());
                // Peak shaving and valley filling by kafka
                kafkaTemplate.send(DataCollectionConstants.DATA_COLLECTION_TOPIC, DealDataUtil.getRowKey(), JSON.toJSONString(data));
                rs.setCode(4000);
                rs.setMsg("Everything is ok!");
                rs.setData(true);
                rs.setSuccess(true);
            }
        }catch (Exception e){
            rs.setCode(5000);
            rs.setMsg("System error!");
            rs.setData(false);
            rs.setSuccess(false);
        }
        return rs;
    }

    @RequestMapping("/pulsar/day")
    public final Result<Boolean> collectionDataByDay(HttpServletRequest request){
        Result<Boolean> rs=new Result<>();
        try {
            // Processing Request Data
            Map<String, String> data = DealDataUtil.dealData(request, dataCollectionInitProperties.getDayHeaders());

            //　If the map is empty, this request will be kill
            if (CollectionUtils.isEmpty(data)) {
                rs.setCode(3000);
                rs.setMsg("Data is Empty!");
                rs.setData(false);
                rs.setSuccess(false);
            }else {
                // Security check
                Long userId = DealDataUtil.getUserIdFromRequest(request);

                // user id
                data.put(DealDataUtil.humpToUnderline(DataCollectionConstants.USER_ID), userId == null ? DataCollectionConstants.ZERO.toString() : userId.toString());
                // rowKey=yyyymmdd concat deviceId
                dataCollectionService.dealCollectionDay(data, DateUtils.getRowKeyDay().concat(data.getOrDefault(DataCollectionConstants.DEVICE_ID,"")));
                rs.setCode(4000);
                rs.setMsg("Everything is ok!");
                rs.setData(true);
                rs.setSuccess(true);
            }
        }catch (Exception e){
            rs.setCode(5000);
            rs.setMsg("System error!");
            rs.setData(false);
            rs.setSuccess(false);
        }
        return rs;
    }
}
