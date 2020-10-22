package net.arksea.ansible.deploy.api.manage.rest;

import net.arksea.ansible.deploy.api.ResultCode;
import net.arksea.ansible.deploy.api.manage.dao.AppTypeDao;
import net.arksea.ansible.deploy.api.manage.entity.App;
import net.arksea.ansible.deploy.api.manage.entity.AppOperation;
import net.arksea.ansible.deploy.api.manage.entity.AppType;
import net.arksea.ansible.deploy.api.manage.service.AppService;
import net.arksea.ansible.deploy.api.manage.service.OperationsService;
import net.arksea.ansible.deploy.api.manage.service.VersionService;
import net.arksea.restapi.BaseResult;
import net.arksea.restapi.ErrorResult;
import net.arksea.restapi.RestResult;
import net.arksea.restapi.RestUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Create by xiaohaixing on 2020/8/28
 */
@RestController
@RequestMapping(value = "/api")
public class AppsController {

    @Autowired
    AppService appService;

    @Autowired
    AppTypeDao appTypeDao;

    @Autowired
    OperationsService operationsService;

    @Autowired
    VersionService versionService;

    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";

    //-------------------------------------------------------------------------
    @RequiresPermissions("应用:修改")
    @RequestMapping(path="apps/template/{typeName}", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public RestResult<App> getAppTemplate(@PathVariable("typeName") String typeName, HttpServletRequest httpRequest) {
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        App app = appService.createAppTemplate(typeName);
        return new RestResult<>(0, app, reqid);
    }

    @RequiresPermissions("应用:修改")
    @RequestMapping(path="apps", method = RequestMethod.POST, produces = MEDIA_TYPE, consumes = MEDIA_TYPE)
    public String save(@RequestBody final App app,final HttpServletRequest httpRequest) {
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        App stored = appService.save(app);
        return RestUtils.createResult(ResultCode.SUCCEED, stored, reqid);
    }

    //-------------------------------------------------------------------------
    @RequiresPermissions("应用:查询")
    @RequestMapping(path="apps/{appId}", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public BaseResult getAppById(@PathVariable("appId") long appId, HttpServletRequest httpRequest) {
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        App app = appService.findOne(appId);
        if (app == null) {
            return new ErrorResult(1, reqid, "应用不存在");
        } else {
            return new RestResult<>(0, app, reqid);
        }
    }
    //-------------------------------------------------------------------------
    @RequiresPermissions("应用:修改")
    @RequestMapping(path="apps/{appId}/deleted", method = RequestMethod.PUT, produces = MEDIA_TYPE)
    public RestResult<Long> updateDeletedById(@PathVariable("appId") long appId,
                                       @RequestBody boolean deleted,
                                       HttpServletRequest httpRequest) {
        appService.updateDeletedById(appId, deleted);
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        return new RestResult<>(0, appId, reqid);
    }
    //-------------------------------------------------------------------------
    @RequiresPermissions("应用:查询")
    @RequestMapping(path="apps/notInGroup", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public RestResult<Iterable<App>> getAppsNotInGroup(HttpServletRequest httpRequest) {
        Iterable<App> apps = appService.findNotInGroup();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        return new RestResult<>(0, apps, reqid);
    }
    //-------------------------------------------------------------------------
    @RequiresPermissions("应用:查询")
    @RequestMapping(path="appTypes", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public RestResult<Iterable<AppType>> getAppTypes(HttpServletRequest httpRequest) {
        Iterable<AppType> types = appTypeDao.findAll();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        return new RestResult<>(0, types, reqid);
    }
    //-------------------------------------------------------------------------
    @RequiresPermissions("操作管理:修改")
    @RequestMapping(path="appTypes/{typeId}", method = RequestMethod.DELETE, produces = MEDIA_TYPE)
    public BaseResult delAppType(@PathVariable(name="typeId") final long typeId,
                                 HttpServletRequest httpRequest) {
        appTypeDao.delete(typeId);
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        return new BaseResult(0, reqid);
    }
    //-------------------------------------------------------------------------
    @RequiresPermissions("应用:查询")
    @RequestMapping(path="appTypes/{appTypeId}/operations", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public RestResult<Iterable<AppOperation>> getOperationsByAppTypeId(
            @PathVariable(name="appTypeId") Long appTypeId,
            final HttpServletRequest httpRequest) {
        Iterable<AppOperation> list = operationsService.getByAppTypeId(appTypeId);
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        return new RestResult<>(0, list, reqid);
    }

}
