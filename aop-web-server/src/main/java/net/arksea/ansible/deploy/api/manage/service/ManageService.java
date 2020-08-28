package net.arksea.ansible.deploy.api.manage.service;

import net.arksea.ansible.deploy.api.auth.dao.UserDao;
import net.arksea.ansible.deploy.api.auth.entity.User;
import net.arksea.ansible.deploy.api.manage.dao.AppGroupDao;
import net.arksea.ansible.deploy.api.manage.entity.AppGroup;
import net.arksea.restapi.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Create by xiaohaixing on 2020/8/21
 */
@Component
public class ManageService {

    public static class AppGroupInfo {
        private String name;       // 分组名称
        private String description;// 分组描述
        private String avatar;     // 分组头像
        private int appCount;      // 分组管理的应用
        private int hostCount;     // 分组管理的主机
        private int userCount;     // 加入分组的用户

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getAppCount() {
            return appCount;
        }

        public void setAppCount(int appCount) {
            this.appCount = appCount;
        }

        public int getHostCount() {
            return hostCount;
        }

        public void setHostCount(int hostCount) {
            this.hostCount = hostCount;
        }

        public int getUserCount() {
            return userCount;
        }

        public void setUserCount(int userCount) {
            this.userCount = userCount;
        }
    }

    @Autowired
    AppGroupDao appGroupDao;

    @Autowired
    UserDao userDao;

    @Transactional
    public AppGroup createGroup(String name, String description) {
        try {
            AppGroup group = new AppGroup();
            group.setName(name);
            group.setDescription(description);
            group.setEnabled(true);
            return appGroupDao.save(group);
        } catch (DataIntegrityViolationException ex) {
            throw new RestException("新建组失败, 可能组名重复或过长", ex);
        } catch (Exception ex) {
            throw new RestException("新建组失败", ex);
        }
    }

    public Iterable<AppGroup> getAppGroups() {
        try {
            return appGroupDao.findAllByEnabled(true);
        } catch (Exception ex) {
            throw new RestException("查询组信息失败", ex);
        }
    }

    @Transactional
    public void deleteAppGroup(long id) {
        try {
            appGroupDao.deleteById(id);
        } catch (Exception ex) {
            throw new RestException("删除组失败", ex);
        }
    }

    public Iterable<User> getUsers(boolean active) {
        try {
            return userDao.findAllByLocked(!active);
        } catch (Exception ex) {
            throw new RestException("查询用户信息失败", ex);
        }
    }

    @Transactional
    public void blockUser(long id) {
        try {
            userDao.lockById(id);
        } catch (Exception ex) {
            throw new RestException("禁用账号失败", ex);
        }
    }

    @Transactional
    public void unblockUser(long id) {
        try {
            userDao.unlockById(id);
        } catch (Exception ex) {
            throw new RestException("启用账号失败", ex);
        }
    }
}