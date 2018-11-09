package com.flyer.job.common.interceptor;

import com.flyer.job.common.annotation.NoNeedLogin;
import com.flyer.job.constants.FlyerConstants;
import com.flyer.job.common.utils.CookieUtil;
import com.flyer.job.common.utils.LoginUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 登录拦截器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Method method = handlerMethod.getMethod();
        final Class<?> clazz = method.getDeclaringClass();

        if (method.isAnnotationPresent(NoNeedLogin.class) || clazz
            .isAnnotationPresent(NoNeedLogin.class)) {
            return true;
        }else {
            String token = CookieUtil.getCookie(request, FlyerConstants.sessionUser);

            if(StringUtils.isNotBlank(token) && LoginUtil.checkSession(LoginUtil.decrypt(token))){
                return true;
            }else {
                //401
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.print("{'code':-1,'msg':'need login!'}");
                writer.flush();
                writer.close();
                return false;
            }
        }
    }

}
