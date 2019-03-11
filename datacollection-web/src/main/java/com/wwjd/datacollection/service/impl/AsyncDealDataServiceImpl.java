package com.wwjd.datacollection.service.impl;

import com.wwjd.datacollection.service.IAsyncDealDataService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * async do something service implement
 *
 * @author 阿导
 * @CopyRight 青团社
 * @Created 2018年12月14日 16:12:00
 */
@Service
public final class AsyncDealDataServiceImpl implements IAsyncDealDataService {

    /**
     * async do something
     *
     * @param map
     * @return
     * @author adao
     * @time 2018/12/14 16:11
     * @CopyRight 杭州弧途科技有限公司（qtshe）
     */
    @Override
    public void dealThirdSomething(Map<String, String> map) {
        // todo: 一些业务若需要数据，可入侵埋点系统，topic,key,system,condition


    }
}
