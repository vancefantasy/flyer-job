package com.flyer.job.web;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.flyer.job.common.BaseController;
import com.flyer.job.common.RestResponse;
import com.flyer.job.common.exception.BizException;
import com.flyer.job.constants.ErrorEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common Controller
 */
@RestController
public class CommonController extends BaseController {

    /**
     * healthCheck
     *
     * @return
     */
    @GetMapping(value = "healthcheck")
    public RestResponse healthCheck() {
        return RestResponse.success().build();
    }

    /**
     * set logger's level
     *
     * @param logger
     * @param level
     * @return
     */
    @GetMapping(value = "setlevel")
    public RestResponse setLoggerLevel(String logger, String level) {
        if (StringUtils.isBlank(logger) || StringUtils.isBlank(level)) {
            BizException.throwOut(ErrorEnum.PARAMETER_ILLEGAL);
        }
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger(logger).setLevel(Level.valueOf(level));
        log.info("logger: {} was set to : {}", logger, level);
        return RestResponse.success().build();
    }

    /**
     * view the current level of logger(s)
     * <p>
     * usage：
     * 1. /loggers : view all logger
     * 2. /loggers/com.flyer.springmvc.rest.web.CommonController : view the specified logger
     *
     * @param name
     * @return
     */
    @GetMapping(value = {"loggers/{name:.+}", "loggers"})
    public RestResponse loggers(@PathVariable(required = false) String name) {
        List<ch.qos.logback.classic.Logger> loggerList = new ArrayList<>();
        List<Map> list = new ArrayList();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        if (StringUtils.isNotBlank(name)) {
            loggerList.add(loggerContext.getLogger(name));
        } else {
            loggerList = loggerContext.getLoggerList();
        }
        for (ch.qos.logback.classic.Logger logger : loggerList) {
            //filter user's logger
            if (!logger.getName().startsWith("com.flyer.springboot.rest")) {
                continue;
            }
            list.add(new HashMap() {{
                put("logger", logger.getName());
                put("level", logger.getEffectiveLevel().levelStr);
            }});
        }
        return RestResponse.success(list).build();
    }

}
