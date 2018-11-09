package com.flyer.job.dao;

import com.flyer.job.domain.FlyerUsers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlyerUsersDao {

    Integer saveFlyerUsers(FlyerUsers flyerUsers);

    Integer updateFlyerUsers(FlyerUsers flyerUsers);

    Integer removeFlyerUsersById(Long id);

    FlyerUsers findFlyerUsersById(Long id);

    List<FlyerUsers> searchPageList(@Param("userName") String userName);

    List<FlyerUsers> getAllFlyerUsers();

    FlyerUsers getUserByUserName(@Param("userName") String userName);
}



