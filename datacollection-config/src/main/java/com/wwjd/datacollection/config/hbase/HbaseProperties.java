package com.wwjd.datacollection.config.hbase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * com.qts.pulsarconfig.hbase configuration properties
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018-11-23 10:10:00
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConfigurationProperties(prefix = "wwjd.hbase")
public final class HbaseProperties {

    /**
     * zookeeper host for com.qts.pulsarconfig.hbase
     */
    private String zookeeperQuorum;
    /**
     * zookeeper port for com.qts.pulsarconfig.hbase
     */
    private String zookeeperClientPort = "2181";
    /**
     * zookeeper directory for com.qts.pulsarconfig.hbase
     */
    private String zookeeperZnodeParent = "/com/wwjd/pulsarconfig/hbase";


    public String getZookeeperQuorum() {
        return zookeeperQuorum;
    }

    public void setZookeeperQuorum(String zookeeperQuorum) {
        this.zookeeperQuorum = zookeeperQuorum;
    }

    public String getZookeeperClientPort() {
        return zookeeperClientPort;
    }

    public void setZookeeperClientPort(String zookeeperClientPort) {
        this.zookeeperClientPort = zookeeperClientPort;
    }

    public String getZookeeperZnodeParent() {
        return zookeeperZnodeParent;
    }

    public void setZookeeperZnodeParent(String zookeeperZnodeParent) {
        this.zookeeperZnodeParent = zookeeperZnodeParent;
    }

//    public String getFsDefaultFs() {
//        return fsDefaultFs;
//    }
//
//    public void setFsDefaultFs(String fsDefaultFs) {
//        this.fsDefaultFs = fsDefaultFs;
//    }
}
