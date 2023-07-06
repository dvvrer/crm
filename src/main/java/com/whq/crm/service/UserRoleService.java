package com.whq.crm.service;

import com.whq.crm.base.BaseService;
import com.whq.crm.dao.UserRoleMapper;
import com.whq.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserRoleService extends BaseService<UserRole,Integer> {

    @Resource
    private UserRoleMapper userRoleMapper;
}
