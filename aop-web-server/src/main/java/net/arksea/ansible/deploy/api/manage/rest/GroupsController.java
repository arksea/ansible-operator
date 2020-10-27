package net.arksea.ansible.deploy.api.manage.rest;

import static net.arksea.ansible.deploy.api.ResultCode.*;
import net.arksea.ansible.deploy.api.auth.service.UserService;
import net.arksea.ansible.deploy.api.manage.entity.AppGroup;
import net.arksea.ansible.deploy.api.manage.service.GroupsService;
import net.arksea.restapi.RestResult;
import net.arksea.restapi.RestUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * Created by xiaohaixing on 2020/04/29.
 */
@RestController
@RequestMapping(value = "/api")
public class GroupsController {
    private static final String MEDIA_TYPE = "application/json; charset=UTF-8";

    @Autowired
    GroupsService groupsService;

    @Autowired
    UserService userService;

    @RequiresPermissions("组管理:修改")
    @RequestMapping(path="groups", method = RequestMethod.POST, produces = MEDIA_TYPE)
    public String createGroup(@RequestParam final String name,
                              @RequestParam final String desc,
                              final HttpServletRequest httpRequest) {
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        AppGroup group = groupsService.createGroup(name, desc);
        return RestUtils.createResult(SUCCEED, group.getId(), reqid);
    }

    @RequiresPermissions("组管理:查询")
    @RequestMapping(path="groups", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public RestResult<Iterable<AppGroup>> getAppGroups(final HttpServletRequest httpRequest) {
        Iterable<AppGroup> groups = groupsService.getAppGroups();
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        return new RestResult<>(SUCCEED, groups, reqid);
    }

    @RequiresPermissions("组管理:查询")
    @RequestMapping(path="groups/{groupId}", method = RequestMethod.GET, produces = MEDIA_TYPE)
    public RestResult<AppGroup> getAppGroups(@PathVariable(name="groupId") long groupId,
                                             final HttpServletRequest httpRequest) {
        AppGroup group = groupsService.getAppGroupById(groupId);
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        return new RestResult<>(SUCCEED, group, reqid);
    }

    @RequiresPermissions("组管理:修改")
    @RequestMapping(path="groups/{groupId}", method = RequestMethod.DELETE, produces = MEDIA_TYPE)
    public String deleteGroup(@PathVariable(name="groupId") long groupId,
                              final HttpServletRequest httpRequest) {
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        groupsService.deleteAppGroup(groupId);
        return RestUtils.createResult(SUCCEED, reqid);
    }

    @RequiresPermissions("组管理:修改")
    @RequestMapping(path="groups/{groupId}/hosts/{hostId}", method = RequestMethod.POST, produces = MEDIA_TYPE)
    public String addHost(@PathVariable(name="groupId") long groupId,
                          @PathVariable(name="hostId") long hostId,
                          final HttpServletRequest httpRequest) {
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        groupsService.addHost(groupId, hostId);
        return RestUtils.createResult(SUCCEED, reqid);
    }

    @RequiresPermissions("组管理:修改")
    @RequestMapping(path="groups/{groupId}/hosts/{hostId}", method = RequestMethod.DELETE, produces = MEDIA_TYPE)
    public String removeHost(@PathVariable(name="groupId") long groupId,
                          @PathVariable(name="hostId") long hostId,
                          final HttpServletRequest httpRequest) {
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        groupsService.removeHost(groupId, hostId);
        return RestUtils.createResult(SUCCEED, reqid);
    }

    @RequiresPermissions("组管理:修改")
    @RequestMapping(path="groups/{groupId}/users/{userId}", method = RequestMethod.POST, produces = MEDIA_TYPE)
    public String addMember(@PathVariable(name="groupId") long groupId,
                          @PathVariable(name="userId") long userId,
                          final HttpServletRequest httpRequest) {
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        groupsService.addMember(groupId, userId);
        return RestUtils.createResult(SUCCEED, reqid);
    }

    @RequiresPermissions("组管理:修改")
    @RequestMapping(path="groups/{groupId}/users/{userId}", method = RequestMethod.DELETE, produces = MEDIA_TYPE)
    public String removeMember(@PathVariable(name="groupId") long groupId,
                             @PathVariable(name="userId") long userId,
                             final HttpServletRequest httpRequest) {
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        groupsService.removeMember(groupId, userId);
        return RestUtils.createResult(SUCCEED, reqid);
    }

    @RequiresPermissions("组管理:修改")
    @RequestMapping(path="groups/{groupId}/apps/{appId}", method = RequestMethod.POST, produces = MEDIA_TYPE)
    public String addApp(@PathVariable(name="groupId") long groupId,
                          @PathVariable(name="appId") long appId,
                          final HttpServletRequest httpRequest) {
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        groupsService.addApp(groupId, appId);
        return RestUtils.createResult(SUCCEED, reqid);
    }

    @RequiresPermissions("组管理:修改")
    @RequestMapping(path="groups/{groupId}/apps/{appId}", method = RequestMethod.DELETE, produces = MEDIA_TYPE)
    public String removeApp(@PathVariable(name="groupId") long groupId,
                             @PathVariable(name="appId") long appId,
                             final HttpServletRequest httpRequest) {
        String reqid = (String)httpRequest.getAttribute("restapi-requestid");
        groupsService.removeApp(groupId, appId);
        return RestUtils.createResult(SUCCEED, reqid);
    }
}
