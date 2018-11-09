package com.flyer.job.web;

import com.flyer.job.common.BaseController;
import com.flyer.job.common.RestResponse;
import com.flyer.job.common.annotation.NoNeedLogin;
import com.flyer.job.domain.FlyerUsers;
import com.flyer.job.service.FlyerUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 用户相关Controller
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private FlyerUsersService flyerUsersService;

    @RequestMapping(value = "listpage")
    public RestResponse listPage(int page, String userName) {
        return flyerUsersService.listPage(page, userName);
    }

    @RequestMapping(value = "add")
    public RestResponse add(FlyerUsers flyerUsers) {
        return flyerUsersService.saveFlyerUsers(flyerUsers);
    }

    @RequestMapping(value = "edit")
    public RestResponse edit(@RequestBody FlyerUsers flyerUsers) {
        return flyerUsersService.updateFlyerUsers(flyerUsers);
    }

    @RequestMapping(value = "remove")
    public RestResponse remove(long id) {
        return flyerUsersService.removeFlyerUsers(id);
    }

    @RequestMapping(value = "list")
    public RestResponse list() {
        return flyerUsersService.list();
    }

    @NoNeedLogin
    @PostMapping(value = "login")
    public RestResponse login(@RequestBody FlyerUsers flyerUsers, HttpServletResponse response) {

        return flyerUsersService.login(flyerUsers, response);
    }
}
