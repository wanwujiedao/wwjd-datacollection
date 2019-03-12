package com.wwjd.datacollection.service.business;

import java.util.List;
import java.util.Map;

/**
 * 过滤器模式接口层，用于处理业务入侵
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2019年03月12日 14:12:00
 */
public interface ICriteria {
    /**
     * 处理过滤规则
     *
     * @author adao
     * @time 2019/3/12 14:18
     * @CopyRight 万物皆导
     * @param mapList
     * @return
     */
    List<Map<String, String>> meetCriteria(List<Map<String, String>> mapList);
}
