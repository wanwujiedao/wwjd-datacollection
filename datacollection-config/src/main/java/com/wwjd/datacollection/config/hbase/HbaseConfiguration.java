package com.wwjd.datacollection.config.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

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
     * 连接 hbase
     *
     * @author adao
     * @time 2019/1/3 10:29
     * @CopyRight 万物皆导
     * @param
     * @return
     */
    @Bean
    public Connection hbaseConnection() throws IOException {
        return ConnectionFactory.createConnection(getConf());
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
//    @Bean
//    public Admin admin() throws IOException {
//        // return hbaseAdmin
//        return hbaseConnection().getAdmin();
//    }

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

        return conf;
    }
}
