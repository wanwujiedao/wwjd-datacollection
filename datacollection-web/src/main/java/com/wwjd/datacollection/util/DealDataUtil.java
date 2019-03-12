package com.wwjd.datacollection.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qts.base.util.JWTUtil;
import com.wwjd.datacollection.constants.DataCollectionConstants;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Processiong some data from requster
 *
 * @author adao
 * @CopyRight qtshe
 * @Created 2018年11月29日 13:35:00
 */
public final class DealDataUtil {
    /**
     * logger
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(DealDataUtil.class);

    private final static BASE64Decoder DECODER = new BASE64Decoder();

    /**
     * processing data from request
     *
     * @param request
     * @param headers
     * @return
     * @author adao
     * @time 2018/11/29 13:35
     * @CopyRight 万物皆导
     */
    public static Map<String, String> dealData(HttpServletRequest request, Set<String> headers) {
        // declare result
        Map<String, String> rs = new HashMap<>(DataCollectionConstants.SIXTEEN);

        // Explicit parameters transmitted directly by data side
        dealExplicitData(request, rs);

        // Implicit parameters transmitted directly by the data side
        dealImplicitData(headers, request, rs);

        // return result
        return rs;
    }

    /**
     * get userId
     *
     * @param request
     * @return
     * @author adao
     * @time 2018/12/17 17:25
     * @CopyRight 万物皆导
     */
    public static Long getUserIdFromRequest(HttpServletRequest request) {
        String auth = request.getHeader(DataCollectionConstants.AUTHORIZATION);
        Long userId = null;
        if (!StringUtils.isEmpty(auth)) {
            String token = auth;
            try {
                //兼容没有加"Bearer "的情况
                if (auth.startsWith(DataCollectionConstants.JWT_PREFIX_BEARER)) {
                    token = auth.substring(DataCollectionConstants.JWT_PREFIX_BEARER.length());
                }
                //兼容截取“Bearer ”后面没有值的情况
                if (StringUtils.isEmpty(token)) {
                    userId = null;
                } else {
                    String sub = JWTUtil.parseJWT(token).getSubject();
                    HashMap map = JSON.parseObject(sub, HashMap.class);

                    Integer userIdStr = (Integer) map.get(DataCollectionConstants.USER_ID);
                    if (userIdStr != null) {
                        userId = Long.valueOf(userIdStr);
                    }
                }
            } catch (Exception c) {
                LOGGER.error(auth, c);
                userId = null;
            }
        }
        return userId;
    }

    /**
     * Implicit parameters transmitted directly by the data side
     *
     * @param request
     * @param rs
     * @return
     * @author adao
     * @time 2018/11/29 14:33
     * @CopyRight 万物皆导
     */
    private static void dealImplicitData(Set<String> headers, HttpServletRequest request, Map<String, String> rs) {
        // if headers is empty
        if (CollectionUtils.isEmpty(headers)) {
            // return
            return;
        }
        //  get header parameter
        headers.forEach(header -> {
            // if header is ip
            if (DataCollectionConstants.IP.equals(header)) {
                // add in map
                rs.put(DataCollectionConstants.IP, getIpAddress(request));
            } else {
                // add in map
                rs.put(header, request.getHeader(header));
            }
        });
    }

    /**
     * Explicit parameters transmitted directly by data side
     *
     * @param request
     * @param rs
     * @return
     * @author adao
     * @time 2018/11/29 14:29
     * @CopyRight 万物皆导
     */
    private static void dealExplicitData(HttpServletRequest request, Map<String, String> rs) {
        // Parameter enumeration
        String spm = request.getParameter(DataCollectionConstants.S_P_M);
        try {
            // Amitabha, turn back to shore
            if (spm == null) {
                return;
            }

            // get json
            String json = new String(Base64.decodeBase64(spm), DataCollectionConstants.UTF_8);

            // if not  null
            if (!StringUtils.isEmpty(json)) {
                // ergodic
                JSONObject.parseObject(json).forEach((key, value) -> {
                    // value is not null
                    if (value != null) {
                        rs.put(humpToUnderline(key), value.toString());
                    }
                });
            }

        } catch (Exception e) {
            LOGGER.error("SPM=[{}]",spm);
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * get real IP
     *
     * @param request
     * @return
     * @author adao
     * @time 2018/11/29 14:50
     * @CopyRight 万物皆导
     */
    private static String getIpAddress(HttpServletRequest request) {

        // Get the IP address of the requesting host, and if it comes in through the proxy, get the real IP address through the firewall.
        String ip = request.getHeader(DataCollectionConstants.HEADER_X_FORWARDED_FOR);

        if (ip == null || ip.length() == DataCollectionConstants.ZERO || DataCollectionConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == DataCollectionConstants.ZERO || DataCollectionConstants.UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader(DataCollectionConstants.HEADER_PROXY_CLIENT_IP);
            }
            if (ip == null || ip.length() == DataCollectionConstants.ZERO || DataCollectionConstants.UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader(DataCollectionConstants.HEADER_WL_PROXY_CLIENT_IP);
            }
            if (ip == null || ip.length() == DataCollectionConstants.ZERO || DataCollectionConstants.UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader(DataCollectionConstants.HEADER_HTTP_CLIENT_IP);
            }
            if (ip == null || ip.length() == DataCollectionConstants.ZERO || DataCollectionConstants.UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader(DataCollectionConstants.HEADER_HTTP_X_FORWARDED_FOR);
            }
            if (ip == null || ip.length() == DataCollectionConstants.ZERO || DataCollectionConstants.UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > DataCollectionConstants.FIFTEEN) {
            String[] ips = ip.split(DataCollectionConstants.COMMA);
            for (int index = DataCollectionConstants.ZERO; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!(DataCollectionConstants.UNKNOWN.equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }


    /**
     * Rowkey charArray
     */
    private static final String ROWKEY_RANDOM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Create Random Rowkey
     *
     * @param
     * @return
     * @author adao
     * @time 2018/12/5 15:54
     * @CopyRight 万物皆导
     */
    public static String getRowKey() {
        // get rowkey-format time
        String rowKey = DateUtils.getRowKeyTime();

        // add five random char
        for (int i = DataCollectionConstants.ZERO; i < DataCollectionConstants.FIVE; i++) {
            // 获取随机数
            rowKey += ROWKEY_RANDOM.charAt(new Random().nextInt(ROWKEY_RANDOM.length()));
        }
        // return rowKey
        return rowKey;
    }


    /**
     * camle name to underline
     *
     * @param para
     * @return
     * @author adao
     * @time 2018/12/6 11:12
     * @CopyRight 万物皆导
     */
    public static String humpToUnderline(String para) {
        // declare result
        StringBuilder sb = new StringBuilder(para);
        // record position from sb
        int temp = DataCollectionConstants.ZERO;
        // ergodic para
        for (int i = DataCollectionConstants.ZERO; i < para.length(); i++) {
            // do core
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, DataCollectionConstants.UNDER_LINE);
                temp += DataCollectionConstants.ONE;
            }
        }
        // return result
        return sb.toString().toLowerCase();
    }


    /**
     * underline name to camle
     *
     * @author adao
     * @time 2019/3/12 13:38
     * @CopyRight 万物皆导
     * @param para
     * @return
     */
    public static String camelName(String para) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (StringUtils.isEmpty(para)) {
            // 没必要转换
            return DataCollectionConstants.BLANK;
        } else if (!para.contains(DataCollectionConstants.UNDER_LINE)) {
            // 不含下划线，仅将首字母小写
            return para.substring(DataCollectionConstants.ZERO, DataCollectionConstants.ONE).toLowerCase() + para.substring(DataCollectionConstants.ONE);
        }
        // 用下划线将原始字符串分割
        String camels[] = para.split(DataCollectionConstants.UNDER_LINE);
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == DataCollectionConstants.ZERO) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(DataCollectionConstants.ZERO, DataCollectionConstants.ONE).toUpperCase());
                result.append(camel.substring(DataCollectionConstants.ONE).toLowerCase());
            }
        }
        return result.toString();
    }
}
