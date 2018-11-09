package com.flyer.job.service;

import com.flyer.job.common.RestResponse;
import com.flyer.job.domain.FlyerUsers;

import javax.servlet.http.HttpServletResponse;

public interface FlyerUsersService {

    RestResponse saveFlyerUsers(FlyerUsers flyerUsers);

    RestResponse removeFlyerUsers(Long flyerUsersId);

    RestResponse updateFlyerUsers(FlyerUsers flyerUsers);

    FlyerUsers findFlyerUsersById(Long flyerUsersId);

    RestResponse listPage(int page, String UserName);

    RestResponse list();

    RestResponse login(FlyerUsers flyerUsers, HttpServletResponse response);
}

