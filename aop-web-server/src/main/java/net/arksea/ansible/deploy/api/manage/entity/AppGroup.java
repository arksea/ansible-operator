package net.arksea.ansible.deploy.api.manage.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import net.arksea.ansible.deploy.api.auth.entity.User;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * 应用分组
 * Create by xiaohaixing on 2020/8/20
 */
@Entity
@Table(name = "dp2_app_group")
public class AppGroup extends IdEntity {
    private String name;       // 分组名称
    private String description;// 分组描述
    private String avatar;     // 分组头像
    private List<App> apps;     // 分组管理的应用
    private Set<Host> hosts;   // 分组管理的主机
    private Set<User> users;   // 加入分组的用户
    private boolean enabled;   // 默认为true，删除或锁定将设置为false

    @NotBlank
    @Column(length = 64, nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 128)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Column(length = 128)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "appGroup",fetch = FetchType.EAGER)
    @JsonManagedReference
    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "dp2_app_group_hosts",
            joinColumns = @JoinColumn(name = "app_group_id"),
            inverseJoinColumns = @JoinColumn(name = "host_id"))
    public Set<Host> getHosts() {
        return hosts;
    }

    public void setHosts(Set<Host> hosts) {
        this.hosts = hosts;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "dp2_app_group_users",
            joinColumns = @JoinColumn(name = "app_group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
