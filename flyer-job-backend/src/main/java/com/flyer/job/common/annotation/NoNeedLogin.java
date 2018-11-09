package com.flyer.job.common.annotation;

import java.lang.annotation.*;

/**
 * 标记不需要登录的url
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoNeedLogin {

}
