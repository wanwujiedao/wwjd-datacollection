package com.wwjd.dc.util;

import com.wwjd.dc.constants.DataCollectionConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * date utils
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018年12月05日 15:27:00
 */
public final class DateUtils {

    /**
     * get default date
     *
     * @param timestampO
     * @return
     * @author adao
     * @time 2018/12/5 15:31
     * @CopyRight 万物皆导
     */
    public static String getDefaultFormatTime(Object timestampO) {
        Long timestamp;
        // if timestampO is null or it's length is less than ten
        if (timestampO == null || timestampO.toString().length() < DataCollectionConstants.TEN) {
            // use current time
            timestamp = System.currentTimeMillis();
        } else {
            // deal timestamp
            timestamp = sToMs((long) timestampO);
        }

        // return result
        return new SimpleDateFormat(DataCollectionConstants.DEFAULT_SDF).format(new Date(timestamp));
    }

    /**
     * get current time
     *
     * @param
     * @return
     * @author adao
     * @time 2018/12/6 14:05
     * @CopyRight 万物皆导
     */
    public static String getNow() {
        // return result
        return new SimpleDateFormat(DataCollectionConstants.CREATE_TIME_SDF).format(new Date());
    }

    /**
     * get time for rowkey
     *
     * @param
     * @return
     * @author adao
     * @time 2018/12/5 15:56
     * @CopyRight 万物皆导
     */
    public static String getRowKeyTime() {
        // return result
        return new SimpleDateFormat(DataCollectionConstants.ROW_KEY_SDF).format(new Date());
    }


    /**
     * s to ms
     *
     * @return
     * @author adao
     * @time 2018/5/18 12:58
     * @CopyRight 万物皆导
     * @Param s
     */
    private static Long sToMs(Long s) {
        // if null
        if (s == null) {
            // return null
            return null;
        }
        // if length eq 13
        if (s.toString().length() == DataCollectionConstants.THIRTEEN) {
            // return result
            return s;
        }
        // return result
        return s * DataCollectionConstants.ONE_THOUSAND;
    }
}
