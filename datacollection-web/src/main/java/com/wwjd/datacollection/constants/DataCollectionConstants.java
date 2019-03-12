package com.wwjd.datacollection.constants;

/**
 * Data Collection's Constants
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018-12-04 14:26:00
 */
public final class DataCollectionConstants {
    /**
     * blank
     */
    public static final String BLANK = "";
    /**
     * json  web token
     */
    public static final String JWT_PREFIX_BEARER = "Bearer";
    /**
     * charset
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * params
     */
    public static final String S_P_M = "spm";
    /**
     * Authorization
     */
    public static final String AUTHORIZATION = "Authorization";
    /**
     * userId
     */
    public static final String USER_ID = "userId";
    /**
     * the topic of send message by kafka
     */
    public static final String DATA_COLLECTION_TOPIC = "data_collect_topic";
    /**
     * event's Formal parameter
     */
    public static final String EVENT_LIST_SIMPLE = "e";
    /**
     * event's Formal parameter
     */
    public static final String EVENT_LIST = "event_list";

    /**
     * tableName
     */
    public static final String HBASE_TABLE_NAME = "data_collection";
    /**
     * tableName
     */
    public static final String HBASE_TABLE_NAME_DAY = "data_collection_day";
    /**
     * a day data family column
     */
    public static final String HBASE_FAMILY_COLUM_DAY="f1";
    /**
     * namespace
     */
    public static final String HBASE_NAMESPACE = "qtshe";
    /**
     * timestamp
     */
    public static final String TIMESTAMP = "timestamp";
    /**
     * date time
     */
    public static final String DATE_TIME = "date_time";
    /**
     * create time
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * json
     */
    public static final String JSON = "json";
    /**
     * under line
     */
    public static final String UNDER_LINE = "_";
    /**
     * IP
     */
    public static final  String IP ="ip";
    /**
     * colon
     */
    public static final String COLON = ":";
    /**
     * comma
     */
    public static final String COMMA  = ",";

    /**
     * Default date format
     */
    public static final String DEFAULT_SDF = "yyyy-MM-dd";

    /**
     * kafka rowKy format
     */
    public static final String ROW_KEY_SDF = "YYMMddHHmmss";
    /**
     * kafka rowKy format
     */
    public static final String ROW_KEY_DAY_SDF = "YYMMdd";
    /**
     * create time format
     */
    public static final String CREATE_TIME_SDF = "yyyy-MM-dd HH:mm:ss";

    /**
     * unknown
     */
    public static final String UNKNOWN = "unknown";
    /**
     * X-Forwarded-For
     */
    public static final String HEADER_X_FORWARDED_FOR="X-Forwarded-For";
    /**
     * Proxy-Client-IP
     */
    public static final String HEADER_PROXY_CLIENT_IP="Proxy-Client-IP";
    /**
     * WL-Proxy-Client-IP
     */
    public static final String HEADER_WL_PROXY_CLIENT_IP="WL-Proxy-Client-IP";
    /**
     * HTTP_CLIENT_IP
     */
    public static final String HEADER_HTTP_CLIENT_IP="HTTP_CLIENT_IP";
    /**
     * HTTP_X_FORWARDED_FOR
     */
    public static final String HEADER_HTTP_X_FORWARDED_FOR="HTTP_X_FORWARDED_FOR";


    /**
     * zero
     */
    public static final Integer ZERO = 0;
    /**
     * one
     */
    public static final int ONE = 1;
    /**
     * FIVE
     */
    public static final int FIVE = 5;
    /**
     * ten
     */
    public final static int TEN = 10;
    /**
     * thirteen
     */
    public final static int THIRTEEN = 13;
    /**
     * fifteen
     */
    public final static int FIFTEEN  = 15;
    /**
     * sixteen
     */
    public final static int SIXTEEN  = 16;
    /**
     * One thousand
     */
    public final static int ONE_THOUSAND =1000;
    /**
     * deviceId
     */
    public final static String DEVICE_ID="device_id";

    /**
     * six point zero
     */
    public final static String SIX_POINT_ZERO = "0.0.0.0";
    /**
     * six zero
     */
    public final static String SIX_ZERO = "000000";
}
