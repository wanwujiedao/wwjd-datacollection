package com.wwjd.datacollection.config.hbase;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * enable com.qts.pulsarconfig.hbase
 *
 * @author adao
 * @CopyRight 万物皆导
 * @Created 2018-11-23 16:22:00
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({HbaseProperties.class, HbaseConfiguration.class})
public @interface EnableHbase {
}
