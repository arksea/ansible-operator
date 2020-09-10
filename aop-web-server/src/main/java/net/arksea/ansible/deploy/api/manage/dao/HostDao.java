package net.arksea.ansible.deploy.api.manage.dao;

import net.arksea.ansible.deploy.api.manage.entity.Host;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HostDao extends CrudRepository<Host, Long> {
    @Modifying
    @Query("update Host h set h.enabled = false where h.id = ?1")
    void deleteById(long id);

    Iterable<Host> findAllByEnabled(boolean enabled);

    @Query("select h from Host h where h.appGroup is null")
    Iterable<Host> findAllGroupIsNull();
}