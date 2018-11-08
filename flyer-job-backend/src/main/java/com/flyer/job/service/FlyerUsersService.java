package com.flyer.job.service;

import com.flyer.job.common.RestResponse;
import com.flyer.job.domain.FlyerUsers;

import javax.servlet.http.HttpServletResponse;


/**
 * 2018-01-24 14:54:12 <br/>
 */
public interface FlyerUsersService {

    RestResponse saveFlyerUsers(FlyerUsers flyerUsers);

    RestResponse removeFlyerUsers(Long flyerUsersId);

    RestResponse updateFlyerUsers(FlyerUsers flyerUsers);

    FlyerUsers findFlyerUsersById(Long flyerUsersId);

    RestResponse listPage(int page, String UserName);

    RestResponse list();

    RestResponse login(FlyerUsers flyerUsers, HttpServletResponse response);
}

