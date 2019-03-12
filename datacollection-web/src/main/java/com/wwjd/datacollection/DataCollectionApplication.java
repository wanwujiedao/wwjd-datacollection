package com.wwjd.datacollection;

import com.wwjd.datacollection.config.hbase.EnableHbase;
import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
@EnableEurekaClient
@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
public class DataCollectionApplication {
    /**
     * main function
     *
     * @param args
     * @return
     * @author adao
     * @time 2018/11/29 15:10
     * @CopyRight 万物皆导
     */
    public static void main(String[] args) {
        // It's time for show.
        SpringApplication.run(DataCollectionApplication.class, args);
    }


    /**
     * 用于接受 shutdown 事件
     */
    @Bean
    public GracefulShutdown gracefulShutdown() {
        return new GracefulShutdown();
    }

    /**
     * tomcat config
     *
     * @author adao
     * @time 2019/2/27 15:26
     * @CopyRight 万物皆导
     * @param
     * @return
     */
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(gracefulShutdown());
        return tomcat;
    }

    /**
     * 优雅关闭 Spring Boot。容器必须是 tomcat
     *
     * @author adao
     * @CopyRight 万物皆导
     * @created 2019/2/27 15:27
     * @Modified_By adao 2019/2/27 15:27
     */

    private class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

        private final Logger log = LoggerFactory.getLogger(GracefulShutdown.class);
        private volatile Connector connector;
        private final int waitTime = 100;

        /**
         * 构造方法
         *
         * @author adao
         * @time 2019/2/27 15:27
         * @CopyRight 万物皆导
         * @param connector
         * @return
         */
        @Override
        public void customize(Connector connector) {
            this.connector = connector;
        }
        /**
         * 重写方法
         *
         * @author adao
         * @time 2019/2/27 15:28
         * @CopyRight 万物皆导
         * @param contextClosedEvent
         * @return
         */
        @Override
        public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
            this.connector.pause();
            Executor executor = this.connector.getProtocolHandler().getExecutor();
            if (executor instanceof ThreadPoolExecutor) {
                try {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                    threadPoolExecutor.shutdown();
                    if (!threadPoolExecutor.awaitTermination(waitTime, TimeUnit.SECONDS)) {
                        log.warn("Tomcat 进程在" + waitTime + " 秒内无法结束，尝试强制结束");
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}

