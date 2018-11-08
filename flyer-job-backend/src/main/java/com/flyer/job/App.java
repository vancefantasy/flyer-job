package com.flyer.job;

import com.flyer.job.constants.FlyerConstants;
import com.flyer.job.core.FlyerServer;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Created by jianying.li on 2018/10/22.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.flyer.job")
@MapperScan("com.flyer.job.dao")
@EnableAsync
public class App extends SpringBootServletInitializer {

    private final static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        //reset quartz config file by environment which default is quartz.properties
        setQuartzConfigFile();

        //spring boot for flyer admin
        SpringApplication.run(App.class, args);

        //flyer server
        FlyerServer.main(args);

        log.info("flyer init");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }

    @Override
    protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
        return super.createRootApplicationContext(servletContext);
    }

    private static void setQuartzConfigFile() {
        //这里把quartz的配置文件和spring boot的配置文件整合在一起
        //将quartz的默认配置文件重置为 application-${spring.profiles.active}.properties 格式
        String activeProfiles = System.getProperty(FlyerConstants.keyOfProfile);

        //未指定 '-Dspring.profiles.active=x' 时，默认是dev环境
        if (StringUtils.isBlank(activeProfiles))
            activeProfiles = FlyerConstants.defaultActiveProfile;

        String fileName = String.format("application-%s.properties", activeProfiles);

        log.info(
            "setQuartzConfigFile, activity profile of property: {}, set org.quartz.properties: {}",
            activeProfiles, fileName);
        System.setProperty(StdSchedulerFactory.PROPERTIES_FILE, fileName);
    }

}
