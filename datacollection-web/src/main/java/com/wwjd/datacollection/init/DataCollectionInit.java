package com.wwjd.datacollection.init;

import com.wwjd.datacollection.config.init.DataCollectionInitProperties;
import com.wwjd.datacollection.constants.DataCollectionConstants;
import com.wwjd.datacollection.service.IHbaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * initualize class
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018-12-04 12:07:00
 */
@Component
public final class DataCollectionInit implements InitializingBean {

    /**
     * com.qts.pulsarconfig.hbase service
     */
    @Autowired
    private IHbaseService hbaseService;
    /**
     * dataCollectionInit properties
     */
    @Autowired
    private DataCollectionInitProperties dataCollectionInitProperties;

    /**
     * logger
     */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    /**
     * set configuration
     *
     * @param
     * @return
     * @author adao
     * @time 2018/12/4 13:40
     * @CopyRight 万物皆导
     */
    private void dealCongig() {
        Map<String, List<String>> familyMappingColumn = dataCollectionInitProperties.getFamilyMappingColumn();
        if(familyMappingColumn!=null&&!familyMappingColumn.isEmpty()) {
            familyMappingColumn.forEach((familyColumn,columns)->{
                if(!CollectionUtils.isEmpty(columns)) {
                    columns.forEach(column-> DataCollectionConfig.mapFamilyColumn.put(column, familyColumn));
                }});
        }
    }


    /**
     * initualize something
     *
     * @param
     * @return
     * @author adao
     * @time 2018/12/5 9:31
     * @CopyRight 万物皆导
     */
    @Override
    public final void afterPropertiesSet() {
        try {
            // com.qts.pulsarconfig.hbase's table check
            hbaseService.creatTable(DataCollectionConstants.HBASE_NAMESPACE, DataCollectionConstants.HBASE_TABLE_NAME, dataCollectionInitProperties.getFamilyMappingColumn().keySet());
            hbaseService.creatTable(DataCollectionConstants.HBASE_NAMESPACE, DataCollectionConstants.HBASE_TABLE_NAME_DAY, new HashSet<String>(){{add(DataCollectionConstants.HBASE_FAMILY_COLUM_DAY);}});
            // processing cofiguration
            dealCongig();

        } catch (Exception e) {
            // record log
            LOGGER.error(e.getMessage(), e);
        }
    }

}
