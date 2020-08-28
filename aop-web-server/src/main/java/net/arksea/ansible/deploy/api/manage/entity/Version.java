package net.arksea.ansible.deploy.api.manage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "dp2_app_version")
public class Version extends IdEntity {
    private String name;

    private String repository;

    private String javaOpt;

    private String revision;

    private Set<Host> targetHosts;

    private App app;

    @NotBlank
    @Column(name = "repo_path", length = 1024, nullable = false)
    public String getRepository() {
        return repository;
    }

    public void setRepository(final String repository) {
        this.repository = repository;
    }

    @Column(name = "java_opt", length = 1024)
    public String getJavaOpt() {
        return javaOpt;
    }

    public void setJavaOpt(final String javaOpt) {
        this.javaOpt = javaOpt;
    }

    @NotBlank
    @Column(nullable = false, columnDefinition = "varchar(64) NOT NULL DEFAULT 'HEAD'")
    public String getRevision() {
        return revision;
    }

    public void setRevision(final String revision) {
        this.revision = revision;
    }

    @ManyToOne
    @JoinColumn(name = "app_id", nullable = false)
    @JsonIgnore
    public App getApp() {
        return app;
    }

    public void setApp(final App app) {
        this.app = app;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "dp2_version_hosts",
            joinColumns = @JoinColumn(name = "version_id"),
            inverseJoinColumns = @JoinColumn(name = "host_id"))
    public Set<Host> getTargetHosts() {
        return targetHosts;
    }

    public void setTargetHosts(final Set<Host> targetHosts) {
        this.targetHosts = targetHosts;
    }

    @NotBlank
    @Column(nullable = false, columnDefinition = "varchar(64) NOT NULL DEFAULT '主版本'")
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}