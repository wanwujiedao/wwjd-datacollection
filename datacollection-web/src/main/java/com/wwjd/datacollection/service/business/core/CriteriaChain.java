package com.wwjd.datacollection.service.business.core;

import com.wwjd.datacollection.service.business.ICriteria;

import java.util.List;
import java.util.Map;

/**
 * 过滤责任链
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2019年03月12日 14:19:00
 */
public final class CriteriaChain implements ICriteria {

    List<ICriteria> criteriaList;

    public CriteriaChain(List<ICriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    /**
     * 处理过滤规则
     *
     * @param mapList
     * @return
     * @author adao
     * @time 2019/3/12 14:18
     * @CopyRight 万物皆导
     */
    @Override
    public List<Map<String, String>> meetCriteria(List<Map<String, String>> mapList) {
        criteriaList.forEach(criteria->criteria.meetCriteria(mapList));
        return mapList;
    }
}
