package com.wwjd.datacollection.service;

import com.wwjd.datacollection.config.hbase.HbaseBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * com.qts.pulsarconfig.hbase service interface
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018年12月05日 10:13:00
 */
public interface IHbaseService {

    /**
     * create com.qts.pulsarconfig.hbase table
     *
     * @author adao
     * @time 2018/12/5 10:39
     * @CopyRight 万物皆导
     * @param nameSpace
     * @param tableName
     * @param familyColumn
     * @throws IOException
     * @return
     */
    void creatTable(String nameSpace, String tableName, Set<String> familyColumn) throws IOException;

    /**
     * batch save data to com.qts.pulsarconfig.hbase
     *
     * @author adao
     * @time 2018/12/5 14:18
     * @CopyRight 万物皆导
     * @param nameSpace
     * @param tableName
     * @param hbaseBeanMap
     * @return
     */
    boolean batchSaveData(String nameSpace, String tableName, Map<String, List<HbaseBean>> hbaseBeanMap);

}
