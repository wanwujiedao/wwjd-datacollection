package com.wwjd.dc.service.impl;

import com.wwjd.config.hbase.HbaseBean;
import com.wwjd.dc.constants.DataCollectionConstants;
import com.wwjd.dc.service.IHbaseService;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
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
public class HbaseServiceImpl implements IHbaseService {

    /**
     * used to  operation com.qts.pulsarconfig.hbase's properties
     */
    @Autowired
    private Admin admin;

    @Autowired
    private HbaseTemplate hbaseTemplate;

    /**
     * create com.qts.pulsarconfig.hbase table
     *
     * @param nameSpace
     * @param tableName
     * @param familyColumns
     * @throws IOException
     * @return
     * @author adao
     * @time 2018/12/5 10:39
     * @CopyRight 万物皆导
     */
    @Override
    public final void creatTable(String nameSpace, String tableName, Set<String> familyColumns) throws IOException {
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
        HTableDescriptor tableDescriptor;
        TableName tableNameReal = TableName.valueOf(realTableName);

        if (!admin.tableExists(tableNameReal)) {
            tableDescriptor = new HTableDescriptor(tableNameReal);
            tableDescriptor.setDurability(Durability.SKIP_WAL);
            familyColumns.stream().forEach(familyColumn -> tableDescriptor.addFamily(new HColumnDescriptor(familyColumn)));
            admin.createTable(tableDescriptor);
        } else {
            tableDescriptor = admin.getTableDescriptor(tableNameReal);
            familyColumns.stream().filter(familyColumn -> !tableDescriptor.hasFamily(familyColumn.getBytes())).forEach(familyColumn -> tableDescriptor.addFamily(new HColumnDescriptor(familyColumn)));
            admin.disableTable(tableNameReal);
            admin.modifyTable(tableNameReal, tableDescriptor);
            admin.enableTable(tableNameReal);

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
        // save new data
        hbaseTemplate.execute(realTableName, hTableInterface -> {
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
                hTableInterface.put(puts);
            }
            // return null
            return null;
        });

        // save data success ,return true
        return true;
    }

}
