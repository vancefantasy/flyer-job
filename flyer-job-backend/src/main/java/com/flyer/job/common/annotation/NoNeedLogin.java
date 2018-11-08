package com.flyer.job.common.annotation;

import java.lang.annotation.*;

/**
 * Created by jianying.li on 2018/10/30.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoNeedLogin {

}
