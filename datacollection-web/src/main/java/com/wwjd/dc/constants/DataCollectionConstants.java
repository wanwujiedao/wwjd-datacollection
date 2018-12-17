package com.wwjd.dc.constants;

/**
 * Data Collection's Constants
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018-12-04 14:26:00
 */
public final class DataCollectionConstants {

    /**
     * json  web token
     */
    public static final String JWT_PREFIX_BEARER = "Bearer";
    /**
     * 编码格式
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * 埋点参数
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
    public static final String EVENT_LIST = "e";

    /**
     * tableName
     */
    public static final String HBASE_TABLE_NAME = "data_collection";
    /**
     * namespace
     */
    public static final String HBASE_NAMESPACE = "万物皆导";
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
    public static final String ROW_KEY_SDF = "yyyyMMddHHmmss";
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
}
