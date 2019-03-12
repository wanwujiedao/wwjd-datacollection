package com.wwjd.datacollection.init;

import java.util.HashMap;
import java.util.Map;

/**
 * some initualize constants
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018-12-04 13:29:00
 */
public final class DataCollectionConfig {


    /**
     * the qualifier mapping to familyColum
     */
    static Map<String, String> mapFamilyColumn = new HashMap<>();


    /**
     * get familyColumn by qualifier
     *
     * @param qualifier
     * @return
     * @author adao
     * @time 2018/12/5 15:20
     * @CopyRight 万物皆导
     */
    public static String getFamilyColumns(String qualifier) {
        // get familyColumn
        return mapFamilyColumn.getOrDefault(qualifier, "f2");
    }
}
