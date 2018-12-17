package com.wwjd.dc;

import com.wwjd.config.hbase.EnableHbase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Data Collection Application
 *
 * @author adao
 * @CopyRight 万物皆导
 * @created 2018/11/29 15:10
 * @Modified_By adao 2018/11/29 15:10
 */
@EnableKafka
@EnableHbase
@EnableAsync
@SpringBootApplication
public class DataCollectionApplication {
    /**
     * main function
     *
     * @author adao
     * @time 2018/11/29 15:10
     * @CopyRight 万物皆导
     * @param args
     * @return
     */
    public static void main(String[] args) {
        // It's time for show.
        SpringApplication.run(DataCollectionApplication.class, args);
    }

}

