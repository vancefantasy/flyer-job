package com.flyer.job.service.impl;

import com.flyer.job.common.RestResponse;
import com.flyer.job.common.exception.BizException;
import com.flyer.job.constants.ErrorEnum;
import com.flyer.job.constants.FlyerConstants;
import com.flyer.job.dao.FlyerUsersDao;
import com.flyer.job.domain.FlyerUsers;
import com.flyer.job.service.FlyerUsersService;
import com.flyer.job.common.utils.CookieUtil;
import com.flyer.job.common.utils.LoginUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.util.*;


/**
 * 2018-01-24 14:54:12<br/>
 */
@Service
public class FlyerUsersServiceImpl implements FlyerUsersService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private FlyerUsersDao flyerUsersDao;

    public RestResponse saveFlyerUsers(FlyerUsers flyerUsers) {
        flyerUsers.setAddTime(new Date());
        flyerUsers.setUserPwd(MD5(flyerUsers.getUserPwd()));
        try {
            int result = flyerUsersDao.saveFlyerUsers(flyerUsers);
            if (result == 0) {
                throw new RuntimeException("FlyerUsers save error result = 0");
            }
        } catch (DuplicateKeyException e) {
            BizException.throwOut(ErrorEnum.RESOURCE_CONFLICT);
        }
        return RestResponse.success(flyerUsers.getId()).build();
    }

    public RestResponse updateFlyerUsers(FlyerUsers flyerUsers) {
        flyerUsers.setUserPwd(MD5(flyerUsers.getUserPwd()));
        int result = flyerUsersDao.updateFlyerUsers(flyerUsers);
        if (result == 0) {
            BizException.throwOut(ErrorEnum.DB_FAIL);
        }
        return RestResponse.success().build();
    }

    public RestResponse removeFlyerUsers(Long flyerUsersId) {
        flyerUsersDao.removeFlyerUsersById(flyerUsersId);
        return RestResponse.success().build();
    }

    public FlyerUsers findFlyerUsersById(Long flyerUsersId) {
        return flyerUsersDao.findFlyerUsersById(flyerUsersId);
    }

    @Override
    public RestResponse listPage(int page, String userName) {
        int pageSize = 10;
        PageHelper.startPage(page, pageSize);

        if (StringUtils.isNotBlank(userName)) {
            userName = "%" + userName + "%";
        }
        List<FlyerUsers> list = flyerUsersDao.searchPageList(userName);
        PageInfo<FlyerUsers> pageInfo = new PageInfo<>(list);
        return RestResponse.success(pageInfo).build();
    }

    @Override
    public RestResponse list() {
        List<FlyerUsers> list = flyerUsersDao.getAllFlyerUsers();
        List<Map> results = new ArrayList<>();
        for (FlyerUsers users : list) {
            Map map = new HashMap();
            map.put("value", users.getUserName());
            map.put("label", users.getUserName());
            results.add(map);
        }
        return RestResponse.success(results).build();
    }

    private String MD5(String pwd) {
        char hexDigits[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] pwdBytes = pwd.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            messageDigest.update(pwdBytes);
            // 获得密文
            byte[] digestBytes = messageDigest.digest();
            // 把密文转换成十六进制的字符串形式
            int j = digestBytes.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = digestBytes[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error("MD5 exception", e);
            throw new RuntimeException("MD5 exception");
        }
    }

    @Override
    public RestResponse login(FlyerUsers flyerUsers, HttpServletResponse response) {
        FlyerUsers user = flyerUsersDao.getUserByUserName(flyerUsers.getUserName());
        if (user == null || !MD5(flyerUsers.getUserPwd()).equals(user.getUserPwd())) {
            BizException.throwOut(ErrorEnum.AUTHENTICATION_FAILED);
        }
        try {
            CookieUtil.setCookie(response, FlyerConstants.sessionUser,
                LoginUtil.encrypt(user.getUserName(), user.getUserPwd()));
        } catch (Exception e) {
            log.error("setCookie error", e);
            BizException.throwOut(ErrorEnum.AUTHENTICATION_FAILED);
        }
        return RestResponse.success(user).build();
    }
}

