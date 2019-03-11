package com.wwjd.datacollection.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qts.datacollection.config.hbase.HbaseBean;
import com.qts.datacollection.config.init.DataCollectionInitProperties;
import com.wwjd.datacollection.constants.DataCollectionConstants;
import com.wwjd.datacollection.init.DataCollectionConfig;
import com.wwjd.datacollection.service.IAsyncDealDataService;
import com.wwjd.datacollection.service.IDataCollectionService;
import com.wwjd.datacollection.service.IHbaseService;
import com.wwjd.datacollection.util.DateUtils;
import com.wwjd.datacollection.util.DealDataUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * data collection service implement
 *
 * @author adao
 * @CopyRight qtshe
 * @Created 2018年12月03日 19:30:00
 */
@Service
public final class DataCollectionServiceImpl implements IDataCollectionService {

    /**
     * autowired com.qts.pulsarconfig.hbase service
     */
    @Autowired
    private IHbaseService hbaseService;
    /**
     * do something asyn
     */
    @Autowired
    private IAsyncDealDataService asyncDealDataService;

    /**
     * autowired init properties
     */
    @Autowired
    private DataCollectionInitProperties dataCollectionInitProperties;

    /**
     * procession kafka message
     *
     * @param record
     * @return
     * @author adao
     * @time 2018/12/3 20:35
     * @CopyRight 杭州弧途科技有限公司（qtshe）
     */
    @Override
    public final boolean dealCollection(ConsumerRecord<String, String> record) {

        // get rowkey prefix
        String keyPre = record.key();
        // get message body
        String value = record.value();
        // If the message body is empty, skip it.
        if (value == null) {
            return true;
        }
        // message body parse to map
        Map<String, String> map = (Map<String, String>) JSONObject.parse(value);
        asyncDealDataService.dealThirdSomething(map);
        // procession something，and save in com.qts.pulsarconfig.hbase
        return hbaseService.batchSaveData(DataCollectionConstants.HBASE_NAMESPACE, DataCollectionConstants.HBASE_TABLE_NAME, DataCollectionServiceHelper.dealMsgData(keyPre, map, dataCollectionInitProperties));

    }

    /**
     * procession data by day
     *
     * @param rowKey
     * @param map
     * @return
     * @author adao
     * @time 2018/12/3 20:35
     * @CopyRight 杭州弧途科技有限公司（qtshe）
     */
    @Override
    public boolean dealCollectionDay(Map<String, String> map,String rowKey) {


        // procession something，and save in com.qts.pulsarconfig.hbase
        return hbaseService.batchSaveData(DataCollectionConstants.HBASE_NAMESPACE, DataCollectionConstants.HBASE_TABLE_NAME_DAY, DataCollectionServiceHelper.dealMsgData(rowKey,map, dataCollectionInitProperties.getParameterMapping(), dataCollectionInitProperties.getPulsarDayWhiteQualifier()));
    }


    /**
     * data clollection helper
     *
     * @author adao
     * @CopyRight 杭州弧途科技有限公司(qtshe)
     * @created 2018/12/6 11:11
     * @Modified_By adao 2018/12/6 11:11
     */
    final static class DataCollectionServiceHelper {


        /**
         * deal msg data
         *
         * @param keyPre
         * @param map
         * @return
         * @author adao
         * @time 2018/12/6 10:25
         * @CopyRight 杭州弧途科技有限公司（qtshe）
         */
        private static Map<String, List<HbaseBean>> dealMsgData(String keyPre, Map<String, String> map, DataCollectionInitProperties dataCollectionInitProperties) {
            // declare result
            Map<String, List<HbaseBean>> hbaseBeanMap = new HashMap<>(map.size());

            // processing common parameters
            List<HbaseBean> list = getCommonParams(map, dataCollectionInitProperties);

            // processing other parameters
            dealOtherParam(keyPre, list, map.getOrDefault(DataCollectionConstants.EVENT_LIST,  map.getOrDefault(DataCollectionConstants.EVENT_LIST_SIMPLE, null)), hbaseBeanMap, dataCollectionInitProperties);

            // return result
            return hbaseBeanMap;
        }

        /**
         * processing other parameters
         *
         * @param keyPre
         * @param list
         * @param eventList
         * @param hbaseBeanMap
         * @return
         * @author adao
         * @time 2018/12/6 11:12
         * @CopyRight 杭州弧途科技有限公司（qtshe）
         */
        private static void dealOtherParam(String keyPre, List<HbaseBean> list, String eventList, Map<String, List<HbaseBean>> hbaseBeanMap, DataCollectionInitProperties dataCollectionInitProperties) {
            // atomic Integer ,record rowKey number
            AtomicReference<Integer> pox = new AtomicReference<>(Integer.valueOf(DataCollectionConstants.ZERO));
            // eventList is not empty
            if (StringUtils.isNotEmpty(eventList)) {
                // parse to list then ergodic
                JSON.parseArray(eventList, String.class).forEach(json -> {
                    // declare new List
                    List<HbaseBean> thisList = new ArrayList<>(list);
                    // not meet timestamp
                    AtomicBoolean flag = new AtomicBoolean(true);
                    // parse jsonObject
                    JSONObject.parseObject(json).forEach((key, value) -> {
                        // get real key
                        String realKey = dataCollectionInitProperties.getEventListMapping().getOrDefault(key, DealDataUtil.humpToUnderline(key));
                        // need to add column dateTime
                        if (flag.get() && DataCollectionConstants.TIMESTAMP.equalsIgnoreCase(realKey) && value != null) {
                            // add date_time
                            addThisList(DataCollectionConstants.DATE_TIME, DateUtils.getDefaultFormatTime(value), thisList, dataCollectionInitProperties.getPulsarWhiteQualifier());
                            // flag set false
                            flag.set(false);
                        }
                        // add in list
                        addThisList(realKey, value, thisList, dataCollectionInitProperties.getPulsarWhiteQualifier());
                    });
                    // this json add in list
                    addThisList(DataCollectionConstants.JSON, json, thisList, dataCollectionInitProperties.getPulsarWhiteQualifier());
                    // add in map
                    hbaseBeanMap.put(keyPre.concat((pox.getAndSet(pox.get() + DataCollectionConstants.ONE)).toString()), thisList);
                });
            } else {
                // add in map
                hbaseBeanMap.put(keyPre.concat(pox.get().toString()), list);
            }
        }

        /**
         * add in list
         *
         * @param key
         * @param value
         * @param list
         * @return
         * @author adao
         * @time 2018/12/6 11:12
         * @CopyRight 杭州弧途科技有限公司（qtshe）
         */
        private static void addThisList(String key, Object value, List<HbaseBean> list,Set<String> pulsarWhiteQualifier) {
            addThisList(DataCollectionConfig.getFamilyColumns(key),key,value,list,pulsarWhiteQualifier);
        }

        /**
         * add in list
         *
         * @param familyColumn
         * @param key
         * @param value
         * @param list
         * @return
         * @author adao
         * @time 2018/12/6 11:12
         * @CopyRight 杭州弧途科技有限公司（qtshe）
         */
        private static void addThisList(String familyColumn, String key, Object value, List<HbaseBean> list, Set<String> qtsQualifier) {
            // if  not null
            if (value != null&&qtsQualifier.contains(key)) {
                // declare hbaseBean
                HbaseBean hbaseBean = new HbaseBean();
                // set familyColumn
                hbaseBean.setFamily(familyColumn);
                // set qualifier
                hbaseBean.setQualifier(key);
                // set value
                hbaseBean.setValue(value.toString());
                // add in list
                list.add(hbaseBean);
            }
        }

        /**
         * processing common parameters
         *
         * @param map
         * @return
         * @author adao
         * @time 2018/12/6 11:12
         * @CopyRight 杭州弧途科技有限公司（qtshe）
         */
        private static List<HbaseBean> getCommonParams(Map<String, String> map, DataCollectionInitProperties dataCollectionInitProperties) {
            // declare hbaseBen List
            List<HbaseBean> hbaseBeans = new ArrayList<>();
            // ergodic parameter's map
            map.forEach((key, value) -> {
                // if key not equalsIgnoreCase eventList
                if (!DataCollectionConstants.EVENT_LIST.equalsIgnoreCase(key)) {
                    addThisList(dataCollectionInitProperties.getParameterMapping().getOrDefault(key, key), value, hbaseBeans, dataCollectionInitProperties.getPulsarWhiteQualifier());
                }
            });
            // add createTime in List
            addThisList(DataCollectionConstants.CREATE_TIME, DateUtils.getNow(), hbaseBeans, dataCollectionInitProperties.getPulsarWhiteQualifier());
            // return hbaseBean list
            return hbaseBeans;
        }

        /**
         * deal map
         *
         * @author adao
         * @time 2019/2/11 16:17
         * @CopyRight 杭州弧途科技有限公司（qts）
         * @param rowKey
         * @param map
         * @return
         */
        private static Map<String, List<HbaseBean>> dealMsgData(String rowKey, Map<String, String> map,Map<String, String> paramMapping,Set<String> pulsarDayWhiteQualifier) {

            // declare result
            Map<String, List<HbaseBean>> hbaseBeanMap = new HashMap<>(map.size());

            List<HbaseBean> list = new ArrayList<>();

            // ergodic parameter's map
            map.forEach((key,val)->addThisList(DataCollectionConstants.HBASE_FAMILY_COLUM_DAY,paramMapping.getOrDefault(key,key),val,list,pulsarDayWhiteQualifier));
            // set val
            hbaseBeanMap.put(rowKey,list);
            // return result
            return hbaseBeanMap;
        }
    }

}
