package com.wwjd.config.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

import java.io.IOException;

/**
 * com.qts.pulsarconfig.hbase congiguration
 *
 * @author adao
 * @CopyRight 万物皆导
 * @created 2018/11/23 10:20
 * @Modified_By adao 2018/11/23 10:20
 */
public final class HbaseConfiguration {

    /**
     * logger
     */
    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * get com.qts.pulsarconfig.hbase's properties
     */
    @Autowired
    private HbaseProperties hbaseProperties;


    /**
     * create hbaseTemplate bean in spring context
     *
     * @param
     * @return
     * @author adao
     * @time 2018/12/5 13:17
     * @CopyRight 万物皆导
     */
    @Bean
    public HbaseTemplate hbaseTemplate() {
        // declare hbaseTemplate
        HbaseTemplate hbaseTemplate = new HbaseTemplate();
        // add com.qts.pulsarconfig.hbase's cofiguration
        hbaseTemplate.setConfiguration(getConf());
        // auto flush
        hbaseTemplate.setAutoFlush(true);
        // return hbaseTemplate
        return hbaseTemplate;
    }

    /**
     * create admin bean in spring context
     *
     * @param
     * @return
     * @author adao
     * @time 2018/12/5 13:17
     * @CopyRight 万物皆导
     */
    @Bean
    public Admin admin() throws IOException {
        // return hbaseAdmin
        return new HBaseAdmin(getConf());
    }

    /**
     * get com.qts.pulsarconfig.hbase configuration
     *
     * @param
     * @return
     * @author adao
     * @time 2018/12/6 13:20
     * @CopyRight 万物皆导
     */
    private Configuration getConf() {
        // create com.qts.pulsarconfig.hbase configuration
        Configuration conf = HBaseConfiguration.create();
        // zk quorum
        conf.set("hbase.zookeeper.quorum", hbaseProperties.getZookeeperQuorum());
        // zk port
        conf.set("hbase.zookeeper.property.clientPort", hbaseProperties.getZookeeperClientPort());
        // zk node
        conf.set("zookeeper.znode.parent", hbaseProperties.getZookeeperZnodeParent());
        // zk master
        conf.set("hbase.master", hbaseProperties.getMaster());
        // hdfs
        conf.set("fs.defaultFS", hbaseProperties.getMaster());
        // return configuration
        return conf;
    }
}
