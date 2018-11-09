package com.flyer.job.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.*;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;

/**
 * 初始化flyer job client
 */
public class FlyerClientContext
    implements DisposableBean, ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private Logger log = LoggerFactory.getLogger(FlyerClientContext.class);

    private FlyerConfig flyerConfig;

    private static ApplicationContext applicationContext;

    private static FlyerClient flyerClient;

    private static boolean haveInit = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null && !haveInit) {
            haveInit = true;

            log.info("Initializing flyer client context");

            //register "FlyerConfig" by manual
            registerFlyerConfigBean();

            //init flyer config
            loadFlyerConfig();

            if (flyerConfig.getDisable()) {
                log.info("NOTE: flyer was set disable");
                return;
            }
            FlyerService flyerService = new FlyerService(flyerConfig);

            flyerClient = new FlyerClient(flyerConfig, flyerService);

            flyerService.setFlyerClient(flyerClient);

            try {
                // init flyer client
                flyerClient.load(true);

                //add shutdown hook
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    public void run() {
                        log.info("flyer client context shutdown hook run");
                        flyerClient.close();
                    }
                }));
                log.info("flyer client initialized");
            } catch (Throwable throwable) {
                log.error("flyer Client load error, config: {}", flyerConfig.toString());
                if (flyerConfig.getDepend()) {
                    log.warn("flyer strong depend");
                    throw new RuntimeException("flyer server not available", throwable);
                } else {
                    log.warn("flyer weak depend", throwable);
                }
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        flyerClient.close();
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }


    public static Map<String, ?> getListByType(Class<?> type) {
        return applicationContext.getBeansOfType(type);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        FlyerClientContext.applicationContext = applicationContext;
    }

    public void registerFlyerConfigBean() {
        ConfigurableApplicationContext configurableApplicationContext =
            (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory =
            (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder =
            BeanDefinitionBuilder.genericBeanDefinition(FlyerConfig.class);
        defaultListableBeanFactory
            .registerBeanDefinition("flyerConfig", beanDefinitionBuilder.getBeanDefinition());
        log.info("register \"FlyerConfig\" to Spring context");
    }

    private void loadFlyerConfig() {
        flyerConfig = applicationContext.getBean(FlyerConfig.class);
        flyerConfig.setConfig();
        log.info("\n flyer client config: {}", flyerConfig.toString());
    }

}
