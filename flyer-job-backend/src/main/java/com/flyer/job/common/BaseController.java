package com.flyer.job.common;

import com.flyer.job.common.exception.BizException;
import com.flyer.job.common.utils.CookieUtil;
import com.flyer.job.common.utils.LoginUtil;
import com.flyer.job.constants.ErrorEnum;
import com.flyer.job.constants.FlyerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

public class BaseController {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected void checkJSR303(BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            Iterator<ObjectError> iterator = result.getAllErrors().iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next().getDefaultMessage());
                if (iterator.hasNext()) {
                    sb.append(",");
                }
            }
            log.warn("checkJSR303 fail, error: {}", sb.toString());
            BizException.throwOut(ErrorEnum.PARAMETER_ILLEGAL);
        }
    }

    protected String getLoginUser(HttpServletRequest request) {
        String token = CookieUtil.getCookie(request, FlyerConstants.sessionUser);
        return LoginUtil.getUserFromToken(token);
    }

}
