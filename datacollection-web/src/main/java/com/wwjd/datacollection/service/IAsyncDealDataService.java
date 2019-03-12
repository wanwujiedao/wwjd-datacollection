package com.wwjd.datacollection.service;

import com.wwjd.datacollection.config.hbase.HbaseBean;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

/**
 * async do something
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018年12月14日 16:09:00
 */
public interface IAsyncDealDataService {

    /**
     * async do something service interface
     *
     * @author adao
     * @time 2018/12/14 16:11
     * @CopyRight 万物皆导
     * @param map
     * @return
     */
    @Async
    void dealThirdSomething(Map<String, List<HbaseBean>> map);
}
