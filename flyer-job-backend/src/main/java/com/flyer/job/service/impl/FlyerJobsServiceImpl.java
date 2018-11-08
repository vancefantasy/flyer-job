package com.flyer.job.service.impl;

import com.flyer.job.common.RestResponse;
import com.flyer.job.constants.FlyerConstants;
import com.flyer.job.core.ConnectionHolder;
import com.flyer.job.core.FlyerConnection;
import com.flyer.job.core.FlyerService;
import com.flyer.job.dao.FlyerJobsDao;
import com.flyer.job.domain.FlyerJobs;
import com.flyer.job.service.FlyerJobsService;
import com.flyer.job.service.QuartzService;
import com.flyer.job.common.utils.JobLogUtils;
import com.flyer.job.common.UID;
import com.flyer.job.common.Utils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


/**
 * 2018-01-24 14:54:12<br/>
 */
@Service
public class FlyerJobsServiceImpl implements FlyerJobsService {

    @Autowired
    private FlyerJobsDao flyerJobsDao;

    @Autowired
    private QuartzService quartzService;

    private final Logger log = LoggerFactory.getLogger(FlyerJobsServiceImpl.class);

    @Override
    public RestResponse saveFlyerJobs(FlyerJobs flyerJobs) {
        flyerJobs.setCreateTime(new Date());
        int result = flyerJobsDao.saveFlyerJobs(flyerJobs);
        if (result == 0) {
            throw new RuntimeException("FlyerJobs save error result = 0");
        }
        return RestResponse.success(flyerJobs.getId()).build();
    }

    @Override
    public RestResponse updateFlyerJobs(FlyerJobs flyerJobs) {
        int result = flyerJobsDao.updateFlyerJobs(flyerJobs);
        if (result == 0) {
            throw new RuntimeException("FlyerJobs update error result = 0");
        }
        return RestResponse.success().build();
    }

    @Override
    public RestResponse removeFlyerJobs(Long flyerJobsId) {

        //停止quartz的任务
        quartzService.pauseJob(flyerJobsId);

        //清理db中的任务数据
        flyerJobsDao.removeFlyerJobsById(flyerJobsId);
        return RestResponse.success().build();
    }

    @Override
    public FlyerJobs findFlyerJobsById(Long flyerJobsId) {
        return flyerJobsDao.findFlyerJobsById(flyerJobsId);
    }

    @Override
    public RestResponse listpage(int page, String jobName, String jobBeanId, String appCode) {
        int pageSize = 10;
        PageHelper.startPage(page, pageSize);

        if (StringUtils.isNotBlank(jobName)) {
            jobName = "%" + jobName + "%";
        }
        if (StringUtils.isNoneBlank(jobBeanId)) {
            jobBeanId = "%" + jobBeanId + "%";
        }

        List<FlyerJobs> list = flyerJobsDao.searchPageList(jobName, jobBeanId, appCode);
        PageInfo<FlyerJobs> pageInfo = new PageInfo<>(list);
        return RestResponse.success(pageInfo).build();
    }

    @Override
    public RestResponse batchRemove(String ids) {
        flyerJobsDao.batchRemoveByIds(ids.split(","));
        return RestResponse.success().build();
    }

    @Override
    public RestResponse operateFlyerJobs(long id, int status) {
        if (status == FlyerConstants.jobStatusStart) {
            quartzService.scheduleJob(id);
        } else {
            quartzService.pauseJob(id);
        }

        //update db
        FlyerJobs flyerJobs = new FlyerJobs();
        flyerJobs.setId(id);
        flyerJobs.setStatus(status);
        int result = flyerJobsDao.updateFlyerJobs(flyerJobs);
        if (result == 0) {
            throw new RuntimeException("operateFlyerJobs update error result = 0");
        }
        return RestResponse.success().build();
    }

    @Override
    public RestResponse runOnce(Long jobId, String connId, String operateUser) {
        log.info("runOnce, jobId: {}, connId: {}", jobId, connId);
        quartzService.triggerJob(jobId, connId, operateUser);
        return RestResponse.success().build();
    }

    @Override
    public RestResponse clients(Long id) {
        FlyerJobs flyerJobs = flyerJobsDao.findFlyerJobsById(id);
        if (flyerJobs == null) {
            log.error("illegal job id : {}", id);
            throw new RuntimeException("illegal job id : " + id);
        }
        List<FlyerConnection> list =
            ConnectionHolder.getConnectionByAppCode(flyerJobs.getAppCode());
        List<Map> clientList = new ArrayList();
        if (list != null) {
            for (FlyerConnection conn : list) {
                Map map = new HashMap<>();
                map.put("id", conn.getConnection().getId());
                map.put("vhost", conn.getVhost());
                map.put("client", Utils.getClientStr(conn.getConnection()));
                map.put("createTime", conn.getCreateTime());
                map.put("lastFlushTime", conn.getLastFlushTime());

                clientList.add(map);
            }
        }
        return RestResponse.success(clientList).build();
    }

    @Override
    public RestResponse cancelJob(Long id, String operateUser) {

        FlyerJobs flyerJobs = flyerJobsDao.findFlyerJobsById(id);

        if (flyerJobs == null) {
            log.error("error job id: {}", id);
            //throws exception
        }

        Long recordId = UID.next();
        flyerJobs.setRecordId(recordId);
        flyerJobs.setExecuteType(FlyerConstants.executeTypeCancel);
        flyerJobs.setOperateUser(operateUser);
        JobLogUtils.addJobRecord(flyerJobs);

        try {
            FlyerService.sendCancelJob(flyerJobs);
        } catch (IOException e) {
            log.error("FlyerService.sendCancelJob error, id: {}", id, e);
        }
        return RestResponse.success().build();
    }

    @Override
    public List<FlyerJobs> listAll() {
        return flyerJobsDao.listAll();
    }

}

