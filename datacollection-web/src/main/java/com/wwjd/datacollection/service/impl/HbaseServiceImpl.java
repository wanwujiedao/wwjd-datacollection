package com.wwjd.datacollection.service.impl;

import com.wwjd.datacollection.config.hbase.HbaseBean;
import com.wwjd.datacollection.constants.DataCollectionConstants;
import com.wwjd.datacollection.service.IHbaseService;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * com.qts.pulsarconfig.hbase service implement
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018年12月05日 10:40:00
 */
@Service
public final class HbaseServiceImpl implements IHbaseService {
    /**
     * logger
     */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Connection hbaseConnection;
    /**
     * create com.qts.pulsarconfig.hbase table
     *
     * @param nameSpace
     * @param tableName
     * @param familyColumns
     * @return
     * @throws IOException
     * @author adao
     * @time 2018/12/5 10:39
     * @CopyRight 万物皆导
     */
    @Override
    public final void creatTable(String nameSpace, String tableName, Set<String> familyColumns) throws IOException {
        Admin admin = hbaseConnection.getAdmin();
        try {
            // 判断表空间是否存在
            if (nameSpace != null) {
                // 默认不存在
                boolean flag = true;
                for (NamespaceDescriptor namespaceDescriptor : admin.listNamespaceDescriptors()) {
                    if (namespaceDescriptor.getName().equals(nameSpace)) {
                        flag = false;
                    }
                }
                if (flag) {
                    admin.createNamespace(NamespaceDescriptor.create(nameSpace).build());
                }
            }
            // 判断表是否存在
            String realTableName = nameSpace == null ? tableName : nameSpace.concat(DataCollectionConstants.COLON).concat(tableName);
            TableDescriptorBuilder tableDescriptor;
            TableName tableNameReal = TableName.valueOf(realTableName);

            if (!admin.tableExists(tableNameReal)) {
                tableDescriptor = TableDescriptorBuilder.newBuilder(tableNameReal);
                tableDescriptor.setDurability(Durability.SKIP_WAL);
                familyColumns.stream().forEach(familyColumn -> tableDescriptor.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(familyColumn)).build()));
                admin.createTable(tableDescriptor.build());
            } else {
                tableDescriptor = TableDescriptorBuilder.newBuilder(admin.getDescriptor(tableNameReal));
                familyColumns.stream().filter(familyColumn -> !tableDescriptor.build().hasColumnFamily(familyColumn.getBytes())).forEach(familyColumn -> tableDescriptor.setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(familyColumn)).build()));
                admin.disableTable(tableNameReal);
                admin.modifyTable(tableDescriptor.build());
                admin.enableTable(tableNameReal);
            }
        } finally {
            admin.close();
        }
    }

    /**
     * batch save data to com.qts.pulsarconfig.hbase
     *
     * @param nameSpace
     * @param tableName
     * @param hbaseBeanMap
     * @return
     * @author adao
     * @time 2018/12/5 14:18
     * @CopyRight 万物皆导
     */
    @Override
    public final boolean batchSaveData(String nameSpace, String tableName, Map<String, List<HbaseBean>> hbaseBeanMap) {
        // get real tableName
        String realTableName = nameSpace == null ? tableName : nameSpace.concat(DataCollectionConstants.COLON).concat(tableName);
        // check data,if null then return false
        if (hbaseBeanMap == null || hbaseBeanMap.isEmpty()) {
            return false;
        }
        // declare puts list
        List<Put> puts = new ArrayList<>();

        // ergodic  datas
        hbaseBeanMap.forEach((rowKey, hbaseBeans) -> {

            if (!CollectionUtils.isEmpty(hbaseBeans)) {
                // set rowKey
                Put put = new Put(rowKey.getBytes());

                // ergodic columns
                hbaseBeans.forEach(hbaseBean -> put.addColumn(hbaseBean.getFamily().getBytes(), hbaseBean.getQualifier().getBytes(), hbaseBean.getValue().getBytes()));

                // add in puts
                puts.add(put);
            }
        });

        // if puts is not  null then batch save datas
        if (!CollectionUtils.isEmpty(puts)) {
            Table table=null;
            // save new data
            try {
                table = hbaseConnection.getTable(TableName.valueOf(realTableName));
                table.put(puts);
                return true;
            } catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
                return false;
            }finally {
                if(table!=null){
                    try {
                        table.close();
                    }catch (Exception e){
                        LOGGER.error(e.getMessage(),e);
                    }
                }
            }

        }


        // save data success ,return true
        return true;
    }

}
