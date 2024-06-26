package com.esi.mscours.msauth.service.impl;

import com.esi.mscours.msauth.dao.RoleDao;
import com.esi.mscours.msauth.entities.Role;
import com.esi.mscours.msauth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public Role findByName(String name) {
        Role role = roleDao.findRoleByName(name);
        return role;
    }
}
