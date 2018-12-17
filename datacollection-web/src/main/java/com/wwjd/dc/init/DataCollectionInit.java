package com.wwjd.dc.init;

import com.wwjd.config.init.DataCollectionInitProperties;
import com.wwjd.dc.constants.DataCollectionConstants;
import com.wwjd.dc.service.IHbaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static com.wwjd.dc.init.PulsarInitConfig.mapFamilyColumn;

/**
 * initualize class
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018-12-04 12:07:00
 */
@Component
public class DataCollectionInit implements InitializingBean {

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
                    columns.forEach(column-> mapFamilyColumn.put(column, familyColumn));
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
            // processing cofiguration
            dealCongig();

        } catch (Exception e) {
            // record log
            LOGGER.error(e.getMessage(), e);
        }
    }

}
