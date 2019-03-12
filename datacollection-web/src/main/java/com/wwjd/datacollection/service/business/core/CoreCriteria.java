package com.wwjd.datacollection.service.business.core;

import com.wwjd.datacollection.constants.DataCollectionConstants;
import com.wwjd.datacollection.service.business.ICriteria;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 核心处理逻辑
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2019年03月12日 14:36:00
 */
public final class CoreCriteria implements ICriteria {

    private String key;

    private String rule;

    public CoreCriteria(String key,String rule) {
        this.key=key;
        this.rule = rule;
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
        Iterator<Map<String, String>> iterator = mapList.iterator();
        Pattern p=Pattern.compile(this.rule);
        while (iterator.hasNext()){
            Map<String, String> map = iterator.next();
            if(!p.matcher(map.getOrDefault(this.key, DataCollectionConstants.BLANK)).matches()){
                iterator.remove();
            }
        }
        return mapList;
    }
}
