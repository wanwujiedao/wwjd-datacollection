package com.wwjd.datacollection.job;

import com.qts.datacollection.config.hbase.HbaseBean;
import com.wwjd.datacollection.constants.DataCollectionConstants;
import com.wwjd.datacollection.service.IHbaseService;
import com.wwjd.datacollection.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每天第一个点入库
 *
 * @author 阿导
 * @CopyRight 青团社
 * @Created 2019年03月05日 09:51:00
 */
@Component
public class EveryDayFirstDataJob {
    /**
     *
     */
    @Autowired
    private IHbaseService hbaseService;


    /**
     * processing task
     *
     * @param
     * @return
     * @author 阿导
     * @time 2019/3/5 11:06
     * @CopyRight 杭州弧途科技有限公司（青团社）
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void dealCore() {
            // set content
            Map<String, List<HbaseBean>> map = new HashMap<>();
            List<HbaseBean> list = new ArrayList<>();
            HbaseBean hbaseBean = new HbaseBean();
            hbaseBean.setFamily(DataCollectionConstants.HBASE_FAMILY_COLUM_DAY);
            hbaseBean.setQualifier(DataCollectionConstants.IP);
            hbaseBean.setValue(DataCollectionConstants.SIX_POINT_ZERO);
            list.add(hbaseBean);
            map.put(DateUtils.getRowKeyDay().concat(DataCollectionConstants.SIX_ZERO), list);

            // procession something，and save in com.qts.pulsarconfig.hbase
            hbaseService.batchSaveData(DataCollectionConstants.HBASE_NAMESPACE, DataCollectionConstants.HBASE_TABLE_NAME, map);
            // procession something，and save in com.qts.pulsarconfig.hbase
            hbaseService.batchSaveData(DataCollectionConstants.HBASE_NAMESPACE, DataCollectionConstants.HBASE_TABLE_NAME_DAY, map);
    }


}
