package com.wwjd.dc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wwjd.config.hbase.HbaseBean;
import com.wwjd.config.init.DataCollectionInitProperties;
import com.wwjd.dc.constants.DataCollectionConstants;
import com.wwjd.dc.init.PulsarInitConfig;
import com.wwjd.dc.service.IAsyncDealDataService;
import com.wwjd.dc.service.IDataCollectionService;
import com.wwjd.dc.service.IHbaseService;
import com.wwjd.dc.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * data collection service implement
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018年12月03日 19:30:00
 */
@Service
public class DataCollectionServiceImpl implements IDataCollectionService {

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
     * @CopyRight 万物皆导
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
     * data clollection helper
     *
     * @author adao
     * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
         */
        private static Map<String, List<HbaseBean>> dealMsgData(String keyPre, Map<String, String> map, DataCollectionInitProperties dataCollectionInitProperties) {
            // declare result
            Map<String, List<HbaseBean>> hbaseBeanMap = new HashMap<>(map.size());

            // processing common parameters
            List<HbaseBean> list = getCommonParams(map, dataCollectionInitProperties);

            // processing other parameters
            dealOtherParam(keyPre, list, map.getOrDefault(DataCollectionConstants.EVENT_LIST, null), hbaseBeanMap, dataCollectionInitProperties);

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
         * @CopyRight 万物皆导
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
                        String realKey = dataCollectionInitProperties.getEventListMapping().getOrDefault(key, key);
                        // need to add column dateTime
                        if (flag.get() && DataCollectionConstants.TIMESTAMP.equalsIgnoreCase(realKey) && value != null) {
                            // add date_time
                            addThisList(DataCollectionConstants.DATE_TIME, DateUtils.getDefaultFormatTime(value), thisList);
                            // flag set false
                            flag.set(false);
                        }
                        // add in list
                        addThisList(realKey, value, thisList);
                    });
                    // this json add in list
                    addThisList(DataCollectionConstants.JSON, json, thisList);
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
         * @CopyRight 万物皆导
         */
        private static void addThisList(String key, Object value, List<HbaseBean> list) {
            // if  not null
            if (value != null) {
                // declare hbaseBean
                HbaseBean hbaseBean = new HbaseBean();
                // set familyColumn
                hbaseBean.setFamily(PulsarInitConfig.getFamilyColumns(key));
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
         * @CopyRight 万物皆导
         */
        private static List<HbaseBean> getCommonParams(Map<String, String> map, DataCollectionInitProperties dataCollectionInitProperties) {
            // declare hbaseBen List
            List<HbaseBean> hbaseBeans = new ArrayList<>();
            // ergodic parameter's map
            map.forEach((key, value) -> {
                // if key not equalsIgnoreCase eventList
                if (!DataCollectionConstants.EVENT_LIST.equalsIgnoreCase(key)) {
                    addThisList(dataCollectionInitProperties.getParameterMapping().getOrDefault(key, key), value, hbaseBeans);
                }
            });
            // add createTime in List
            addThisList(DataCollectionConstants.CREATE_TIME, DateUtils.getNow(), hbaseBeans);
            // return hbaseBean list
            return hbaseBeans;
        }

        /**
         * camle name to underline
         *
         * @param para
         * @return
         * @author adao
         * @time 2018/12/6 11:12
         * @CopyRight 万物皆导
         */
        private static String humpToUnderline(String para) {
            // declare result
            StringBuilder sb = new StringBuilder(para);
            // record position from sb
            int temp = DataCollectionConstants.ZERO;
            // ergodic para
            for (int i = DataCollectionConstants.ZERO; i < para.length(); i++) {
                // do core
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, DataCollectionConstants.UNDER_LINE);
                    temp += DataCollectionConstants.ONE;
                }
            }
            // return result
            return sb.toString().toLowerCase();
        }

    }

}
