package com.flyer.job.core;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Quartz 配置类
 */
@Configuration
public class QuartzConfig {

    @Bean(name = "SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        String fileName = System.getProperty(StdSchedulerFactory.PROPERTIES_FILE);
        //see https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
        //1 file:./config/application.properties
        //2 file:./application.properties
        //3 classpath:application.properties
        Resource resource = new ClassPathResource("file:" + "config" + File.separator + fileName);
        if (!resource.exists()) {
            resource =   new ClassPathResource("file:" + fileName);
            if (!resource.exists()){
                resource = new ClassPathResource(fileName);
            }
        }
        propertiesFactoryBean.setLocation(resource);
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean
    public Scheduler scheduler() throws Exception {
        return schedulerFactoryBean().getScheduler();
    }
}
